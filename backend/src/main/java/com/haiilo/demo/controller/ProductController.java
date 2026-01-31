package com.haiilo.demo.controller;

import com.haiilo.demo.dto.ProductRequest;
import com.haiilo.demo.dto.ProductResponse;
import com.haiilo.demo.entity.Product;
import com.haiilo.demo.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @Operation(summary = "Create a new product", description = "Adds a new product to the supermarket inventory")
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        Product product = Product.builder()
                .name(request.name())
                .unitPrice(request.unitPrice())
                .build();

        Product savedProduct = productService.createProduct(product);

        return new ResponseEntity<>(
                new ProductResponse(savedProduct.getId(), savedProduct.getName(), savedProduct.getUnitPrice()),
                HttpStatus.CREATED
        );
    }

    @Operation(summary = "Get all products", description = "Retrieves a list of all products in the supermarket inventory")
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> products = productService.findAll().stream()
                .map(p -> new ProductResponse(p.getId(), p.getName(), p.getUnitPrice()))
                .toList();
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Delete product", description = "Removes a specific product by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
