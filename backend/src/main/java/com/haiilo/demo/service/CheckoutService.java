package com.haiilo.demo.service;

import com.haiilo.demo.dto.CheckoutResponse;

import java.util.List;

public interface CheckoutService {
    CheckoutResponse calculateTotalFromIds(List<Long> productIds);
}
