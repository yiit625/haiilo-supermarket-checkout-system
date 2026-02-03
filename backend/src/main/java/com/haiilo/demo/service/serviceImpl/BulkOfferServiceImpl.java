package com.haiilo.demo.service.serviceImpl;

import com.haiilo.demo.entity.BulkOffer;
import com.haiilo.demo.repository.BulkOfferRepository;
import com.haiilo.demo.service.BulkOfferService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BulkOfferServiceImpl implements BulkOfferService {

    private final BulkOfferRepository bulkOfferRepository;

    @Override
    public BulkOffer createOffer(BulkOffer offer) {
        validateOfferUniqueness(offer.getProduct().getId(), offer.getProduct().getName());
        return bulkOfferRepository.save(offer);
    }

    @Override
    public Optional<BulkOffer> getOfferByProductId(Long productId) {
        return bulkOfferRepository.findByProductId(productId);
    }

    @Override
    public List<BulkOffer> findAll() {
        return bulkOfferRepository.findAll();
    }

    @Override
    @Transactional // Atomic
    public void deleteById(Long id) {
        BulkOffer offer = bulkOfferRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Offer not found with id: " + id));

        if (offer.getProduct() != null) {
            offer.getProduct().setBulkOffer(null);
        }

        bulkOfferRepository.delete(offer);
    }

    private void validateOfferUniqueness(Long productId, String productName) {
        bulkOfferRepository.findByProductId(productId)
                .ifPresent(existingOffer -> {
                    throw new IllegalStateException("A bulk offer already exists for this product: " + productName);
                });
    }
}
