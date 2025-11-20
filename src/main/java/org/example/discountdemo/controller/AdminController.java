package org.example.discountdemo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.discountdemo.common.ApiResponse;
import org.example.discountdemo.entity.CouponTemplate;
import org.example.discountdemo.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@Tag(name = "管理后台接口")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private CouponService couponService;

    @PostMapping("/coupons")
    @Operation(summary = "创建优惠券")
    public ApiResponse<CouponTemplate> create(@RequestBody CouponTemplate template) {
        return ApiResponse.success(couponService.createTemplate(template));
    }

    @GetMapping("/coupons")
    @Operation(summary = "查看所有优惠券")
    public ApiResponse<List<CouponTemplate>> list() {
        return ApiResponse.success(couponService.findAllTemplates());
    }
}