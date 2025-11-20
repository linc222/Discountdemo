package org.example.discountdemo.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.example.discountdemo.entity.CouponTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CouponTemplateRepo extends JpaRepository<CouponTemplate, Long> {

    // 【核心】乐观锁扣减库存
    // 只有当 available_stock > 0 时才更新，返回更新行数
    @Modifying
    @Query("UPDATE CouponTemplate c SET c.availableStock = c.availableStock - 1 " +
            "WHERE c.id = :id AND c.availableStock > 0")
    int decreaseStock(@Param("id") Long id);
}