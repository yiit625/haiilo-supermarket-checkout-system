package com.haiilo.demo.dto;

import java.math.BigDecimal;

public record ItemDetail(
        String productName,
        int quantity,
        BigDecimal priceBeforeDiscount,
        BigDecimal priceAfterDiscount,
        boolean discountApplied
) {}
