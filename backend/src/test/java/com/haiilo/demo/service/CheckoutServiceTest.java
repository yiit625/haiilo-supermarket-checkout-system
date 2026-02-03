package com.haiilo.demo.service;

import com.haiilo.demo.dto.CheckoutResponse;
import com.haiilo.demo.dto.ItemDetail;
import com.haiilo.demo.entity.BulkOffer;
import com.haiilo.demo.entity.Product;
import com.haiilo.demo.service.serviceImpl.CheckoutServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CheckoutServiceTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private CheckoutServiceImpl checkoutService;

    @Test
    void shouldCalculateTotalFromIdsWithOffers() {
        // GIVEN
        Product apple = Product.builder().id(1L).name("Apple").unitPrice(new BigDecimal("0.30")).build();
        apple.setBulkOffer(BulkOffer.builder().requiredQuantity(3).offerPrice(new BigDecimal("0.75"))
                .expiryDate(LocalDateTime.now().plusWeeks(1)).build());

        Product banana = Product.builder().id(2L).name("Banana").unitPrice(new BigDecimal("0.50")).build();

        List<Long> productIds = List.of(1L, 1L, 1L, 2L);
        when(productService.findAllByIdsWithOffers(anySet())).thenReturn(List.of(apple, banana));

        // WHEN
        CheckoutResponse response = checkoutService.calculateTotalFromIds(productIds);

        // THEN
        assertThat(response.finalTotal()).isEqualByComparingTo("1.25");
        assertThat(response.items()).hasSize(2);
        ItemDetail appleDetail = response.items().stream().filter(i -> i.productName().equals("Apple")).findFirst().get();
        assertThat(appleDetail.discountApplied()).isTrue();
        assertThat(appleDetail.priceAfterDiscount()).isEqualByComparingTo("0.75");
        assertThat(appleDetail.priceBeforeDiscount()).isEqualByComparingTo("0.90");
    }

    @Test
    void shouldCalculateTotalWithComplexScenario() {
        // GIVEN
        Product apple = Product.builder().id(1L).name("Apple").unitPrice(new BigDecimal("0.30")).build();
        apple.setBulkOffer(BulkOffer.builder().requiredQuantity(3).offerPrice(new BigDecimal("0.75"))
                .expiryDate(LocalDateTime.now().plusWeeks(1)).build());

        Product egg = Product.builder().id(3L).name("Egg").unitPrice(new BigDecimal("0.45")).build();
        egg.setBulkOffer(BulkOffer.builder().requiredQuantity(3).offerPrice(new BigDecimal("1.15"))
                .expiryDate(LocalDateTime.now().plusWeeks(1)).build());

        Product banana = Product.builder().id(2L).name("Banana").unitPrice(new BigDecimal("0.50")).build();

        List<Long> productIds = List.of(1L, 1L, 1L, 2L, 3L, 3L, 3L, 3L, 3L);
        when(productService.findAllByIdsWithOffers(anySet())).thenReturn(List.of(apple, banana, egg));

        // WHEN
        CheckoutResponse response = checkoutService.calculateTotalFromIds(productIds);

        // THEN: 0.75 (Apple) + 0.50 (Banana) + 2.05 (Egg) = 3.30
        assertThat(response.finalTotal()).isEqualByComparingTo("3.30");
        assertThat(response.items().stream().filter(ItemDetail::discountApplied)).hasSize(2); // Apple ve Egg
    }

    @Test
    void shouldNotApplyOfferWhenItIsExpired() {
        // GIVEN
        Product apple = Product.builder().id(1L).name("Apple").unitPrice(new BigDecimal("0.30")).build();
        apple.setBulkOffer(BulkOffer.builder().requiredQuantity(3).offerPrice(new BigDecimal("0.75"))
                .expiryDate(LocalDateTime.now().minusDays(1)).build());

        List<Long> productIds = List.of(1L, 1L, 1L);
        when(productService.findAllByIdsWithOffers(anySet())).thenReturn(List.of(apple));

        // WHEN
        CheckoutResponse response = checkoutService.calculateTotalFromIds(productIds);

        // THEN
        assertThat(response.finalTotal()).isEqualByComparingTo("0.90");
        assertThat(response.items().get(0).discountApplied()).isFalse();
    }
}