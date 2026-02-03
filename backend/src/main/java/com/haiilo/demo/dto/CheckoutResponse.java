package com.haiilo.demo.dto;

import java.math.BigDecimal;
import java.util.List;

public record CheckoutResponse(
        List<ItemDetail> items,
        BigDecimal finalTotal
) {}
