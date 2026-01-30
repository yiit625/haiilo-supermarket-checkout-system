package com.haiilo.demo.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank(message = "Product name cannot be blank")
        String name,

        @NotNull(message = "Unit price is required")
        @DecimalMin(value = "0.01", message = "Unit price must be positive")
        BigDecimal unitPrice
) {}