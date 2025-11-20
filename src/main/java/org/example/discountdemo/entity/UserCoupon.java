package org.example.discountdemo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Entity
@Data
@Table(name = "user_coupon",
        uniqueConstraints = @UniqueConstraint(columnNames = {"userId", "templateId", "couponCode"}))
// 实际场景中如果允许领多张，unique索引要调整，这里简化为防止重复提交的兜底
public class UserCoupon {
    @Getter
    @Setter
    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @ManyToOne
    @JoinColumn(name = "template_id")
    private CouponTemplate template;

    // 1-未使用, 2-已使用, 3-已过期
    private Integer status;

    private LocalDateTime receiveTime;
    private LocalDateTime useTime;

    private Long orderId; // 核销关联的订单ID

}