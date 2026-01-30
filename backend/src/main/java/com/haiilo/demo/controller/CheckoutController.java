package com.haiilo.demo.controller;

import com.haiilo.demo.dto.CheckoutRequest;
import com.haiilo.demo.entity.Product;
import com.haiilo.demo.service.CheckoutService;
import com.haiilo.demo.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/checkout")
@RequiredArgsConstructor
public class CheckoutController {
    private final CheckoutService checkoutService;
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<BigDecimal> calculateTotal(@Valid @RequestBody CheckoutRequest request) {
        List<Product> products = request.productIds().stream()
                .map(productService::findById)
                .toList();

        BigDecimal total = checkoutService.calculateTotal(products);
        return ResponseEntity.ok(total);
    }
}
