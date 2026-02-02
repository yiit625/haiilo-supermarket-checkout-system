package com.haiilo.demo.service;

import com.haiilo.demo.entity.BulkOffer;
import com.haiilo.demo.entity.Product;
import com.haiilo.demo.service.serviceImpl.CheckoutServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
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
        // GIVEN: Apple (w/campaign) and Banana (no/campaign)
        Product apple = Product.builder()
                .id(1L)
                .name("Apple")
                .unitPrice(new BigDecimal("0.30"))
                .build();

        BulkOffer appleOffer = BulkOffer.builder()
                .requiredQuantity(3)
                .offerPrice(new BigDecimal("0.75"))
                .build();
        apple.setBulkOffer(appleOffer);

        Product banana = Product.builder()
                .id(2L)
                .name("Banana")
                .unitPrice(new BigDecimal("0.50"))
                .build();

        List<Long> productIds = List.of(1L, 1L, 1L, 2L);

        when(productService.findAllByIdsWithOffers(anySet())).thenReturn(List.of(apple, banana));

        // WHEN
        BigDecimal total = checkoutService.calculateTotalFromIds(productIds);

        // THEN: 0.75 (3 Apples) + 0.50 (1 Banana) = 1.25
        assertThat(total).isEqualByComparingTo("1.25");
    }

    @Test
    void shouldCalculateTotalWithComplexScenario() {
        // GIVEN: Apple (3 for 0.75) and Egg (3 for 1.15)
        Product apple = Product.builder().id(1L).unitPrice(new BigDecimal("0.30")).build();
        apple.setBulkOffer(BulkOffer.builder().requiredQuantity(3).offerPrice(new BigDecimal("0.75")).build());

        Product egg = Product.builder().id(3L).unitPrice(new BigDecimal("0.45")).build();
        egg.setBulkOffer(BulkOffer.builder().requiredQuantity(3).offerPrice(new BigDecimal("1.15")).build());

        Product banana = Product.builder().id(2L).unitPrice(new BigDecimal("0.50")).build();

        // 3 Apple, 5 Egg, 1 Banana
        List<Long> productIds = List.of(1L, 1L, 1L, 2L, 3L, 3L, 3L, 3L, 3L);

        when(productService.findAllByIdsWithOffers(anySet())).thenReturn(List.of(apple, banana, egg));

        // WHEN
        BigDecimal total = checkoutService.calculateTotalFromIds(productIds);

        // THEN:
        // 3 Apple = 0.75
        // 1 Banana = 0.50
        // 5 Egg = (1 * 1.15) + (2 * 0.45) = 1.15 + 0.90 = 2.05
        // Total = 0.75 + 0.50 + 2.05 = 3.30
        assertThat(total).isEqualByComparingTo("3.30");
    }

    @Test
    void shouldThrowExceptionWhenSomeProductsNotFound() {
        // GIVEN
        List<Long> productIds = List.of(1L, 999L);

        // IN DB there is only product with ID 1
        Product apple = Product.builder().id(1L).name("Apple").unitPrice(new BigDecimal("0.30")).build();

        // Mock: DB returns only the found product
        when(productService.findAllByIdsWithOffers(anySet())).thenReturn(List.of(apple));

        // WHEN & THEN: We wait for EntityNotFoundException
        assertThatThrownBy(() -> checkoutService.calculateTotalFromIds(productIds))
                .isInstanceOf(EntityNotFoundException.class);
    }
}