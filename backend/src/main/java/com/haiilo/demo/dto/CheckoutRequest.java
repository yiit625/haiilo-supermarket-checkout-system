package com.haiilo.demo.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CheckoutRequest(
        @NotEmpty(message = "Product list cannot be empty")
        @Size(max = 500, message = "You cannot checkout more than 500 items at once")
        List<@NotNull(message = "Product ID cannot be null") Long> productIds
) {}