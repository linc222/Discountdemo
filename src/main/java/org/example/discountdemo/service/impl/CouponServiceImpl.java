package org.example.discountdemo.service.impl;

import org.example.discountdemo.common.BusinessException;
import org.example.discountdemo.entity.CouponTemplate;
import org.example.discountdemo.entity.UserCoupon;
import org.example.discountdemo.repository.CouponTemplateRepo;
import org.example.discountdemo.repository.UserCouponRepo;
import org.example.discountdemo.service.CouponService;
import org.example.discountdemo.service.strategy.CalculationStrategy;
import org.example.discountdemo.service.strategy.StrategyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CouponServiceImpl implements CouponService {

    @Autowired private CouponTemplateRepo templateRepo;
    @Autowired private UserCouponRepo userCouponRepo;
    @Autowired private StrategyFactory strategyFactory;
    @Autowired private StringRedisTemplate redisTemplate;

    @Override
    public CouponTemplate createTemplate(CouponTemplate template) {
        template.setAvailableStock(template.getTotalStock());
        return templateRepo.save(template);
    }

    @Override
    public List<CouponTemplate> findAllTemplates() {
        return templateRepo.findAll();
    }

    @Override
    public List<UserCoupon> findUserCoupons(Long userId) {
        return userCouponRepo.findByUserId(userId);
    }

    @Override
    @Transactional
    public void acquireCoupon(Long userId, Long templateId) {
        // 1. Redis 防刷 (5秒限制)
        String lockKey = "lock:coupon:" + userId;
        Boolean locked = redisTemplate.opsForValue().setIfAbsent(lockKey, "1", 5, TimeUnit.SECONDS);
        if (Boolean.FALSE.equals(locked)) {
            throw new BusinessException("操作太频繁，请稍后");
        }

        CouponTemplate template = templateRepo.findById(templateId)
                .orElseThrow(() -> new BusinessException("优惠券不存在"));

        // 2. 校验时间
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(template.getStartTime()) || now.isAfter(template.getEndTime())) {
            throw new BusinessException("不在领取时间内");
        }

        // 3. 校验限领
        long count = userCouponRepo.countByUserIdAndTemplateId(userId, templateId);
        if (count >= template.getLimitPerUser()) {
            throw new BusinessException("超出限领数量");
        }

        // 4. 乐观锁扣减库存
        int rows = templateRepo.decreaseStock(templateId);
        if (rows == 0) {
            throw new BusinessException("优惠券已抢光");
        }

        // 5. 发券
        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setUserId(userId);
        userCoupon.setTemplate(template);
        userCoupon.setStatus(1); // 1-未使用
        userCoupon.setReceiveTime(LocalDateTime.now());
        userCouponRepo.save(userCoupon);
    }

    @Override
    @Transactional
    public BigDecimal checkout(Long userId, Long userCouponId, BigDecimal amount) {
        UserCoupon userCoupon = userCouponRepo.findById(userCouponId)
                .orElseThrow(() -> new BusinessException("优惠券不存在"));

        if (!userCoupon.getUserId().equals(userId)) {
            throw new BusinessException("不是你的优惠券");
        }
        if (userCoupon.getStatus() != 1) {
            throw new BusinessException("优惠券已使用或无效");
        }

        // 策略模式计算
        CalculationStrategy strategy = strategyFactory.get(userCoupon.getTemplate().getType());
        if (strategy == null) {
            throw new BusinessException("未知的优惠券类型");
        }

        BigDecimal finalPrice = strategy.calculate(amount, userCoupon.getTemplate());

        // 更新状态
        userCoupon.setStatus(2); // 2-已使用
        userCoupon.setUseTime(LocalDateTime.now());
        userCouponRepo.save(userCoupon);

        return finalPrice;
    }
}