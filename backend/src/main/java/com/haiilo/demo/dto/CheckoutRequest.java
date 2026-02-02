package com.haiilo.demo.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CheckoutRequest(
        @NotEmpty(message = "Product list cannot be empty")
        List<@NotNull(message = "Product ID cannot be null") Long> productIds
) {}