package com.haiilo.demo.service;

import com.haiilo.demo.entity.Product;

import java.math.BigDecimal;
import java.util.List;

public interface CheckoutService {
    BigDecimal calculateTotal(List<Product> products);
}
