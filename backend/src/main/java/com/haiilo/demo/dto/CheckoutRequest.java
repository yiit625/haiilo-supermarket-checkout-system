package com.haiilo.demo.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record CheckoutRequest(
        @NotEmpty(message = "Product list cannot be empty")
        List<Long> productIds
) {}