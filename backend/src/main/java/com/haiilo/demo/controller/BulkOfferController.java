package com.haiilo.demo.controller;

import com.haiilo.demo.dto.BulkOfferRequest;
import com.haiilo.demo.entity.BulkOffer;
import com.haiilo.demo.entity.Product;
import com.haiilo.demo.service.BulkOfferService;
import com.haiilo.demo.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/offers")
@RequiredArgsConstructor
public class BulkOfferController {

    private final BulkOfferService bulkOfferService;
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<BulkOffer> createOffer(@Valid @RequestBody BulkOfferRequest request) {
        Product product = productService.findById(request.productId());

        BulkOffer offer = BulkOffer.builder()
                .product(product)
                .requiredQuantity(request.requiredQuantity())
                .offerPrice(request.offerPrice())
                .build();

        return new ResponseEntity<>(bulkOfferService.createOffer(offer), HttpStatus.CREATED);
    }
}
