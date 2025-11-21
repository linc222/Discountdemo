package org.example.discountdemo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.discountdemo.common.ApiResponse;
import org.example.discountdemo.entity.UserCoupon;
import org.example.discountdemo.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/app")
@Tag(name = "用户端接口")
@CrossOrigin(origins = "*")
public class AppController {

    @Autowired
    private CouponService couponService;

    @PostMapping("/coupons/{id}/acquire")
    @Operation(summary = "领取优惠券")
    public ApiResponse<Void> acquire(@RequestHeader("X-User-Id") Long userId,
                                     @PathVariable Long id) {
        couponService.acquireCoupon(userId, id);
        return ApiResponse.success();
    }

    @GetMapping("/my-coupons")
    @Operation(summary = "我的优惠券")
    public ApiResponse<List<UserCoupon>> myCoupons(@RequestHeader("X-User-Id") Long userId) {
        return ApiResponse.success(couponService.findUserCoupons(userId));
    }

    @PostMapping("/checkout")
    @Operation(summary = "下单核销")
    public ApiResponse<BigDecimal> checkout(@RequestHeader("X-User-Id") Long userId,
                                            @RequestBody CheckoutRequest request) {
        BigDecimal finalPrice = couponService.checkout(userId, request.getCouponId(), request.getAmount());
        return ApiResponse.success(finalPrice);
    }
}