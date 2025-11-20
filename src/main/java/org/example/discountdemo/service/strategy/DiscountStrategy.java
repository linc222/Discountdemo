package org.example.discountdemo.service.strategy;

import org.example.discountdemo.common.BusinessException;
import org.example.discountdemo.entity.CouponTemplate;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class DiscountStrategy implements CalculationStrategy {
    @Override
    public Integer getType() { return 2; } // 2代表折扣

    @Override
    public BigDecimal calculate(BigDecimal orderTotal, CouponTemplate template) {
        if (orderTotal.compareTo(template.getMinThreshold()) < 0) {
            throw new BusinessException("未达到折扣金额门槛");
        }
        // 总价 * 折扣率，保留2位小数
        return orderTotal.multiply(template.getValue()).setScale(2, RoundingMode.HALF_UP);
    }
}