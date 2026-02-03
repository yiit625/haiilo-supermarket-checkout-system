package com.haiilo.demo.service.serviceImpl;

import com.haiilo.demo.dto.CheckoutResponse;
import com.haiilo.demo.dto.ItemDetail;
import com.haiilo.demo.entity.BulkOffer;
import com.haiilo.demo.entity.Product;
import com.haiilo.demo.service.CheckoutService;
import com.haiilo.demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CheckoutServiceImpl implements CheckoutService {
    private final ProductService productService;

    /**
     * Calculates the total price for a list of product IDs, applying any relevant discounts.
     *
     * @param productIds List of product IDs to calculate the total for.
     * @return CheckoutResponse containing item details and final total price.
     */
    @Override
    public CheckoutResponse calculateTotalFromIds(List<Long> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            return new CheckoutResponse(Collections.emptyList(), BigDecimal.ZERO);
        }

        Map<Long, Long> productCounts = productIds.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        List<Product> products = productService.findAllByIdsWithOffers(productCounts.keySet());

        List<ItemDetail> details = products.stream()
                .map(p -> createItemDetail(p, productCounts.get(p.getId()).intValue()))
                .collect(Collectors.toList());

        BigDecimal finalTotal = details.stream()
                .map(ItemDetail::priceAfterDiscount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CheckoutResponse(details, finalTotal);
    }

    /**
     * Creates an ItemDetail object for a given product and quantity.
     *
     * @param product  The product to create the detail for.
     * @param quantity The quantity of the product.
     * @return ItemDetail containing pricing information.
     */
    private ItemDetail createItemDetail(Product product, int quantity) {
        BigDecimal priceBefore = calculateRegularPrice(product, quantity);
        BigDecimal priceAfter = calculateItemTotal(product, quantity);

        return new ItemDetail(
                product.getName(),
                quantity,
                priceBefore,
                priceAfter,
                priceAfter.compareTo(priceBefore) < 0
        );
    }

    /**
     * Calculates the total price for a single product item, considering bulk offers if applicable.
     *
     * @param product  The product to calculate the total for.
     * @param quantity The quantity of the product.
     * @return Total price for the given product and quantity.
     */
    private BigDecimal calculateItemTotal(Product product, int quantity) {
        if (product.getBulkOffer() != null && product.getBulkOffer().getExpiryDate().isAfter(LocalDateTime.now())) {
            return applyBulkDiscount(product, quantity, product.getBulkOffer());
        }
        return calculateRegularPrice(product, quantity);
    }

    /**
     * Calculates the regular price for a given product and quantity without any discounts.
     *
     * @param product  The product to calculate the price for.
     * @param quantity The quantity of the product.
     * @return Regular price for the given product and quantity.
     */
    private BigDecimal calculateRegularPrice(Product product, int quantity) {
        return product.getUnitPrice().multiply(BigDecimal.valueOf(quantity));
    }

    /**
     * Applies bulk discount to the product price based on the provided bulk offer.
     *
     * @param product  The product to apply the discount to.
     * @param quantity The quantity of the product.
     * @param offer    The bulk offer to apply.
     * @return Total price after applying the bulk discount.
     */
    private BigDecimal applyBulkDiscount(Product product, int quantity, BulkOffer offer) {
        int batches = quantity / offer.getRequiredQuantity();
        int remainder = quantity % offer.getRequiredQuantity();

        BigDecimal bulkPrice = offer.getOfferPrice().multiply(BigDecimal.valueOf(batches));
        BigDecimal regularPrice = product.getUnitPrice().multiply(BigDecimal.valueOf(remainder));

        return bulkPrice.add(regularPrice);
    }
}