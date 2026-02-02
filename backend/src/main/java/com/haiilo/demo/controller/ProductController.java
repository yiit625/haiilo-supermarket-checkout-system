package com.haiilo.demo.controller;

import com.haiilo.demo.dto.ProductRequest;
import com.haiilo.demo.dto.ProductResponse;
import com.haiilo.demo.entity.Product;
import com.haiilo.demo.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "Get products with paging and optional search", description = "Retrieves a paged list of products, optionally filtered by name.")
    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search) {
        Page<Product> productsPage;
        if (search != null && !search.trim().isEmpty()) {
            productsPage = productService.searchProductsByName(search, page, size);
        } else {
            productsPage = productService.findAllByPaging(page, size);
        }

        Page<ProductResponse> productResponses = productsPage.map(p -> new ProductResponse(p.getId(), p.getName(), p.getUnitPrice()));
        return ResponseEntity.ok(productResponses);
    }

    @Operation(summary = "Delete product", description = "Removes a specific product by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
