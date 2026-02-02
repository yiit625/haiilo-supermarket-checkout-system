package com.haiilo.demo.service;

import com.haiilo.demo.entity.Product;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

public interface ProductService {

    Product createProduct(Product product);

    Page<Product> findAllByPaging(int page, int size);

    Page<Product> searchProductsByName(String name, int page, int size);

    Product findById(Long id);

    List<Product> findAllByIdsWithOffers(Set<Long> ids);

    void deleteById(Long id);
}
