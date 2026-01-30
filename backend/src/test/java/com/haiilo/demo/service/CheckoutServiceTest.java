package com.haiilo.demo.service;

import com.haiilo.demo.entity.BulkOffer;
import com.haiilo.demo.entity.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckoutServiceTest {

    @Mock
    private BulkOfferService bulkOfferService;

    @InjectMocks
    private CheckoutServiceImpl checkoutService;

    @Test
    void shouldCalculateTotalWithMixedItemsAndOffers() {
        // GIVEN: 3 Apple (w/campaign) ve 1 Muz (wout/campaign)
        Product apple = Product.builder().id(1L).name("Apple").unitPrice(new BigDecimal("0.30")).build();
        Product banana = Product.builder().id(2L).name("Banana").unitPrice(new BigDecimal("0.50")).build();

        List<Product> cart = List.of(apple, apple, apple, banana);

        // Apple campaign: 3 unit 0.75 EUR
        BulkOffer appleOffer = BulkOffer.builder()
                .product(apple)
                .requiredQuantity(3)
                .offerPrice(new BigDecimal("0.75"))
                .build();

        when(bulkOfferService.getOfferByProductId(1L)).thenReturn(Optional.of(appleOffer));
        when(bulkOfferService.getOfferByProductId(2L)).thenReturn(Optional.empty());

        // WHEN
        BigDecimal total = checkoutService.calculateTotal(cart);

        // THEN: 0.75 (Apples) + 0.50 (Banana) = 1.25
        assertThat(total).isEqualByComparingTo("1.25");
    }
}