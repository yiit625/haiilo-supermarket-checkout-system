package com.haiilo.demo.repository;

import com.haiilo.demo.entity.BulkOffer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BulkOfferRepository extends JpaRepository<BulkOffer, Long> {
    Optional<BulkOffer> findByProductId(Long productId);
}
