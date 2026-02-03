package com.haiilo.demo.controller;

import com.haiilo.demo.dto.CheckoutRequest;
import com.haiilo.demo.dto.CheckoutResponse;
import com.haiilo.demo.service.CheckoutService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/checkout")
@RequiredArgsConstructor
public class CheckoutController {
    private final CheckoutService checkoutService;

    @Operation(summary = "Calculate total price", description = "Calculates the total price for a list of product IDs, applying any relevant discounts")
    @PostMapping
    public ResponseEntity<CheckoutResponse> calculateTotal(@Valid @RequestBody CheckoutRequest request) {
        return ResponseEntity.ok(checkoutService.calculateTotalFromIds(request.productIds()));
    }
}
