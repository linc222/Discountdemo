package org.example.discountdemo.service.strategy;

import org.example.discountdemo.entity.CouponTemplate;
import java.math.BigDecimal;

public interface CalculationStrategy {
    BigDecimal calculate(BigDecimal orderTotal, CouponTemplate template);
    Integer getType();
}