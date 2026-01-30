package com.haiilo.demo.service;

import com.haiilo.demo.entity.Product;
import com.haiilo.demo.repository.ProductRepository;
import com.haiilo.demo.service.serviceImpl.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void shouldSaveProductSuccessfully() {
        // GIVEN
        Product product = Product.builder()
                .name("Apple")
                .unitPrice(new BigDecimal("0.30"))
                .build();

        when(productRepository.save(any(Product.class))).thenReturn(product);

        // WHEN
        Product savedProduct = productService.createProduct(product);

        // THEN
        assertThat(savedProduct.getName()).isEqualTo("Apple");
        assertThat(savedProduct.getUnitPrice()).isEqualByComparingTo("0.30");
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void shouldFindAllProductsSuccessfully() {
        // GIVEN
        Product product1 = Product.builder()
                .name("Apple")
                .unitPrice(new BigDecimal("0.30"))
                .build();

        Product product2 = Product.builder()
                .name("Banana")
                .unitPrice(new BigDecimal("0.20"))
                .build();

        when(productRepository.findAll()).thenReturn(List.of(product1, product2));

        // WHEN
        List<Product> products = productService.findAll();

        // THEN
        assertThat(products).hasSize(2);
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void shouldFindProductByIdSuccessfully() {
        // GIVEN
        Long productId = 1L;
        Product product = Product.builder()
                .id(productId)
                .name("Apple")
                .unitPrice(new BigDecimal("0.30"))
                .build();

        when(productRepository.findById(productId)).thenReturn(java.util.Optional.of(product));

        // WHEN
        Product foundProduct = productService.findById(productId);

        // THEN
        assertThat(foundProduct.getId()).isEqualTo(productId);
        assertThat(foundProduct.getName()).isEqualTo("Apple");
        verify(productRepository, times(1)).findById(productId);
    }
}