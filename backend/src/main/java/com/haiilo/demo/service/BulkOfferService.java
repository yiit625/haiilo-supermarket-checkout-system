package com.haiilo.demo.service;

import com.haiilo.demo.entity.BulkOffer;

import java.util.List;
import java.util.Optional;

public interface BulkOfferService {
    BulkOffer createOffer(BulkOffer offer);
    Optional<BulkOffer> getOfferByProductId(Long productId);

    List<BulkOffer> findAll();
}
