package org.example.discountdemo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "coupon_template")
public class CouponTemplate {
    @Getter
    @Setter
    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    // 1-满减, 2-折扣
    private Integer type;

    // 满减金额 或 折扣率(0.85)
    private BigDecimal value;

    // 最低消费门槛
    private BigDecimal minThreshold;

    private Integer totalStock;

    // 核心：库存字段
    private Integer availableStock;

    // 每人限领
    private Integer limitPerUser;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

}