package com.haiilo.demo.controller;

import com.haiilo.demo.dto.BulkOfferRequest;
import com.haiilo.demo.entity.BulkOffer;
import com.haiilo.demo.entity.Product;
import com.haiilo.demo.service.BulkOfferService;
import com.haiilo.demo.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/offers")
@RequiredArgsConstructor
public class BulkOfferController {

    private final BulkOfferService bulkOfferService;
    private final ProductService productService;

    @Operation(summary = "Create a new bulk offer", description = "Creates a new bulk purchase offer for a specific product")
    @PostMapping
    public ResponseEntity<BulkOffer> createOffer(@Valid @RequestBody BulkOfferRequest request) {
        Product product = productService.findById(request.productId());

        BulkOffer offer = BulkOffer.builder()
                .product(product)
                .requiredQuantity(request.requiredQuantity())
                .offerPrice(request.offerPrice())
                .expiryDate(request.expiryDate())
                .build();

        return new ResponseEntity<>(bulkOfferService.createOffer(offer), HttpStatus.CREATED);
    }

    @Operation(summary = "Get all bulk offers", description = "Retrieves a list of all bulk purchase offers")
    @GetMapping
    public ResponseEntity<Iterable<BulkOffer>> getAllOffers() {
        return new ResponseEntity<>(bulkOfferService.findAll(), HttpStatus.OK);
    }

    @Operation(summary = "Delete bulk offer", description = "Removes a specific bulk offer by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOffer(@PathVariable Long id) {
        bulkOfferService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
