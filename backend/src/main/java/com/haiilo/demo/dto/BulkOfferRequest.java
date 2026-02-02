package com.haiilo.demo.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BulkOfferRequest(
        @NotNull Long productId,
        @NotNull @Min(2) Integer requiredQuantity,
        @NotNull @DecimalMin("0.01") BigDecimal offerPrice,
        LocalDateTime expiryDate
) {}
