package com.haiilo.demo.dto;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String name,
        BigDecimal unitPrice
) {}