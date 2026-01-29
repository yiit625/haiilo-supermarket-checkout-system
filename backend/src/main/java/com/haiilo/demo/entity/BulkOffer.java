package com.haiilo.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "bulk_offers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BulkOffer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne // suppose each product can have only one bulk offer
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Min(value = 1, message = "Required quantity must be at least 1")
    private int requiredQuantity;

    @NotNull(message = "Offer price is required")
    @DecimalMin(value = "0.01", message = "Offer price must be positive")
    private BigDecimal offerPrice;
}