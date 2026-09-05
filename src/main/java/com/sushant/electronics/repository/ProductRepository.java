package com.sushant.electronics.repository;

import com.sushant.electronics.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Spring Boot:
 * Spring Data JPA persistence abstraction.
 *
 * SAP Commerce equivalent:
 * There is no direct ProductRepository equivalent.
 * SAP Commerce normally uses DAO + FlexibleSearch.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByCode(String code);

    boolean existsByCode(String code);

    Page<Product> findByCodeContainingIgnoreCaseOrNameContainingIgnoreCase(
            String code,
            String name,
            Pageable pageable);

    /**
     * Atomic inventory deduction used during checkout.
     * The stock check and decrement happen in one database statement,
     * preventing two concurrent checkouts from overselling the same stock.
     */
    @Modifying
    @Query(value = "UPDATE products SET stock = stock - :quantity " +
            "WHERE id = :productId AND stock >= :quantity", nativeQuery = true)
    int decreaseStockIfAvailable(@Param("productId") Long productId,
                                 @Param("quantity") Integer quantity);
}
