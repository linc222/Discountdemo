package org.example.discountdemo.controller;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CheckoutRequest {
    private Long couponId;
    private BigDecimal amount;
}