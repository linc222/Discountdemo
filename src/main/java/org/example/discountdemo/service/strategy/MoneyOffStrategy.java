package org.example.discountdemo.service.strategy;

import org.example.discountdemo.common.BusinessException;
import org.example.discountdemo.entity.CouponTemplate;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class MoneyOffStrategy implements CalculationStrategy {
    @Override
    public Integer getType() { return 1; } // 1代表满减

    @Override
    public BigDecimal calculate(BigDecimal orderTotal, CouponTemplate template) {
        if (orderTotal.compareTo(template.getMinThreshold()) < 0) {
            throw new BusinessException("未达到满减金额门槛");
        }
        // 减去优惠金额，如果小于0则为0
        return orderTotal.subtract(template.getValue()).max(BigDecimal.ZERO);
    }
}