package com.haiilo.demo.service;

import com.haiilo.demo.entity.Product;

import java.util.List;

public interface ProductService {

    Product createProduct(Product product);

    List<Product> findAll();

    Product findById(Long id);

    void deleteById(Long id);
}
