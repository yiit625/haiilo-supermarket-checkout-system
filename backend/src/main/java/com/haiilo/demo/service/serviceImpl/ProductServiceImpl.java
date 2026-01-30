package com.haiilo.demo.service.serviceImpl;

import com.haiilo.demo.entity.Product;
import com.haiilo.demo.repository.ProductRepository;
import com.haiilo.demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }
}
