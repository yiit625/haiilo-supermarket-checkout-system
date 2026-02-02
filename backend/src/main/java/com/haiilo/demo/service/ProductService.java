package com.haiilo.demo.service;

import com.haiilo.demo.entity.Product;

import java.util.List;
import java.util.Set;

public interface ProductService {

    Product createProduct(Product product);

    List<Product> findAll();

    Product findById(Long id);

    List<Product> findAllByIdsWithOffers(Set<Long> ids);

    void deleteById(Long id);
}
