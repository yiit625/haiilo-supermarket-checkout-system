package com.haiilo.demo.service.serviceImpl;

import com.haiilo.demo.entity.BulkOffer;
import com.haiilo.demo.entity.Product;
import com.haiilo.demo.service.BulkOfferService;
import com.haiilo.demo.service.CheckoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CheckoutServiceImpl implements CheckoutService {
    private final BulkOfferService bulkOfferService;

    /**
     * Calculates the total price of the given products, considering available bulk discounts.
     * * @param products The list of products in the shopping cart.
     * @return The total price as a {@link BigDecimal} scaled for currency.
     */
    @Override
    public BigDecimal calculateTotal(List<Product> products) {
        if (products == null || products.isEmpty()) {
            return BigDecimal.ZERO;
        }

        // 1. Product id - quantity
        Map<Product, Long> productCounts = products.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        // 2. Calculate price for every group and sum
        return productCounts.entrySet().stream()
                .map(entry -> calculateItemTotal(entry.getKey(), entry.getValue().intValue()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Calculates the total price for a specific product and quantity, applying bulk discounts if available.
     * @param product Product entity
     * @param quantity Quantity of the product
     * @return Total price as {@link BigDecimal}
     */
    private BigDecimal calculateItemTotal(Product product, int quantity) {
        return bulkOfferService.getOfferByProductId(product.getId())
                .map(offer -> applyBulkDiscount(product, quantity, offer))
                .orElseGet(() -> calculateRegularPrice(product, quantity));
    }

    /**
     * Calculates the regular price without any discounts.
     * @param product Product entity
     * @param quantity Quantity of the product
     * @return Total price as {@link BigDecimal}
     */
    private BigDecimal calculateRegularPrice(Product product, int quantity) {
        return product.getUnitPrice().multiply(BigDecimal.valueOf(quantity));
    }

    /**
     * Applies the bulk discount to the product based on the offer.
     * @param product Product entity
     * @param quantity Quantity of the product
     * @param offer BulkOffer entity
     * @return Total price as {@link BigDecimal}
     */
    private BigDecimal applyBulkDiscount(Product product, int quantity, BulkOffer offer) {
        int batches = quantity / offer.getRequiredQuantity();
        int remainder = quantity % offer.getRequiredQuantity();

        BigDecimal bulkPrice = offer.getOfferPrice().multiply(BigDecimal.valueOf(batches));
        BigDecimal regularPrice = product.getUnitPrice().multiply(BigDecimal.valueOf(remainder));

        return bulkPrice.add(regularPrice);
    }
}
