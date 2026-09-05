package com.sushant.electronics.repository;

import com.sushant.electronics.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

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
}