package com.haiilo.demo.service;

import com.haiilo.demo.entity.Product;
import com.haiilo.demo.repository.ProductRepository;
import com.haiilo.demo.service.serviceImpl.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

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
    void shouldFindAllProductsByPagingSuccessfully() {
        // GIVEN
        int page = 0;
        int size = 10;
        Product product = Product.builder().name("Apple").build();
        Page<Product> productPage = new PageImpl<>(List.of(product));

        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());

        when(productRepository.findAll(pageable)).thenReturn(productPage);

        // WHEN
        Page<Product> result = productService.findAllByPaging(page, size);

        // THEN
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Apple");
        verify(productRepository).findAll(pageable);
    }

    @Test
    void shouldSearchProductsByNameSuccessfully() {
        // GIVEN
        String searchTerm = "app";
        int page = 0;
        int size = 10;
        Product product = Product.builder().name("Apple").build();
        Page<Product> productPage = new PageImpl<>(List.of(product));

        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());

        when(productRepository.findByNameContainingIgnoreCase(eq(searchTerm), eq(pageable)))
                .thenReturn(productPage);

        // WHEN
        Page<Product> result = productService.searchProductsByName(searchTerm, page, size);

        // THEN
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Apple");
        verify(productRepository).findByNameContainingIgnoreCase(searchTerm, pageable);
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

    @Test
    void shouldDeleteProductByIdSuccessfully() {
        // GIVEN
        Long productId = 1L;


        when(productRepository.existsById(productId)).thenReturn(true);

        productService.deleteById(productId);

        verify(productRepository, times(1)).existsById(productId);
        verify(productRepository, times(1)).deleteById(productId);
    }
}