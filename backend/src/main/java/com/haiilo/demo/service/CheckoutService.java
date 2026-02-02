package com.haiilo.demo.service;

import java.math.BigDecimal;
import java.util.List;

public interface CheckoutService {
    BigDecimal calculateTotalFromIds(List<Long> productIds);
}
