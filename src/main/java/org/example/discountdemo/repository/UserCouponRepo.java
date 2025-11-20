package org.example.discountdemo.repository;

import org.example.discountdemo.entity.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserCouponRepo extends JpaRepository<UserCoupon, Long> {

    // 统计用户已领某张券的数量
    long countByUserIdAndTemplateId(Long userId, Long templateId);

    // 【核心修复】你需要补上这个方法定义，Spring Data JPA 才会自动生成 SQL
    List<UserCoupon> findByUserId(Long userId);
}