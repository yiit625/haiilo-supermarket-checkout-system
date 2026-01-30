package com.haiilo.demo.service.serviceImpl;

import com.haiilo.demo.entity.BulkOffer;
import com.haiilo.demo.repository.BulkOfferRepository;
import com.haiilo.demo.service.BulkOfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BulkOfferServiceImpl implements BulkOfferService {

    private final BulkOfferRepository bulkOfferRepository;

    @Override
    public BulkOffer createOffer(BulkOffer offer) {
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
}
