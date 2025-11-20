package org.example.discountdemo.service;

import org.example.discountdemo.entity.CouponTemplate;
import org.example.discountdemo.entity.UserCoupon;
import java.math.BigDecimal;
import java.util.List;

public interface CouponService {
    CouponTemplate createTemplate(CouponTemplate template);
    void acquireCoupon(Long userId, Long templateId);
    BigDecimal checkout(Long userId, Long userCouponId, BigDecimal amount);
    List<CouponTemplate> findAllTemplates();
    List<UserCoupon> findUserCoupons(Long userId);
}