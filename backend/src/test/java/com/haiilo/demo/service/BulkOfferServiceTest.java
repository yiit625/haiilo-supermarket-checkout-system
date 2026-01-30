package com.haiilo.demo.service;

import com.haiilo.demo.entity.BulkOffer;
import com.haiilo.demo.entity.Product;
import com.haiilo.demo.repository.BulkOfferRepository;
import com.haiilo.demo.service.serviceImpl.BulkOfferServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BulkOfferServiceTest {

    @Mock
    private BulkOfferRepository bulkOfferRepository;

    @InjectMocks
    private BulkOfferServiceImpl bulkOfferService;

    @Test
    void shouldCreateOfferSuccessfully() {
        // GIVEN
        Product apple = Product.builder().id(1L).name("Apple").unitPrice(new BigDecimal("0.30")).build();
        BulkOffer offer = BulkOffer.builder()
                .product(apple)
                .requiredQuantity(3)
                .offerPrice(new BigDecimal("0.75"))
                .build();

        when(bulkOfferRepository.save(any(BulkOffer.class))).thenReturn(offer);

        // WHEN
        BulkOffer savedOffer = bulkOfferService.createOffer(offer);

        // THEN
        assertThat(savedOffer.getRequiredQuantity()).isEqualTo(3);
        assertThat(savedOffer.getOfferPrice()).isEqualByComparingTo("0.75");
        verify(bulkOfferRepository, times(1)).save(any(BulkOffer.class));
    }

    @Test
    void shouldGetOfferByProductId() {
        // GIVEN
        Long productId = 1L;
        Product apple = Product.builder().id(productId).name("Apple").unitPrice(new BigDecimal("0.30")).build();
        BulkOffer expectedOffer = BulkOffer.builder()
                .product(apple)
                .requiredQuantity(3)
                .offerPrice(new BigDecimal("0.75"))
                .build();

        when(bulkOfferRepository.findByProductId(productId)).thenReturn(Optional.of(expectedOffer));

        // WHEN
        Optional<BulkOffer> actualOffer = bulkOfferService.getOfferByProductId(productId);

        // THEN
        assertThat(actualOffer).isPresent();
        assertThat(actualOffer.get().getOfferPrice()).isEqualByComparingTo("0.75");
        verify(bulkOfferRepository, times(1)).findByProductId(productId);
    }

    @Test
    void shouldGetAllOffers() {
        // GIVEN
        when(bulkOfferRepository.findAll()).thenReturn(java.util.List.of());

        // WHEN
        List<BulkOffer> offers = bulkOfferService.findAll();

        // THEN
        assertThat(offers).isEmpty();
        verify(bulkOfferRepository, times(1)).findAll();
    }
}
