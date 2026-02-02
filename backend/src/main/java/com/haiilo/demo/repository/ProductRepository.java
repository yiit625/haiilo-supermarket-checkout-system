package com.haiilo.demo.repository;

import com.haiilo.demo.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Set;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.bulkOffer WHERE p.id IN :ids")
    List<Product> findAllByIdWithOffers(@Param("ids") Set<Long> ids);

    @Override
    @NonNull
    Page<Product> findAll(@NonNull Pageable pageable);

    @NonNull
    Page<Product> findByNameContainingIgnoreCase(@NonNull String name, @NonNull Pageable pageable);
}
