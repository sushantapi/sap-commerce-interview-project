package com.sushant.electronics.dao;

import com.sushant.electronics.entity.Product;

import java.util.List;
import java.util.Optional;

/**
 * SAP Commerce:
 * DAO layer responsible for product persistence operations.
 *
 * Spring Boot:
 * Interface that abstracts the persistence implementation.
 *
 * In SAP Commerce, the DAO would typically use FlexibleSearch
 * to retrieve ProductModel objects.
 *
 * In our Spring Boot project, ProductDaoImpl uses
 * ProductRepository/JPA underneath.
 */
public interface ProductDao {

    /**
     * SAP Commerce:
     * Save a ProductModel using the persistence layer.
     *
     * Spring Boot:
     * Delegates to JpaRepository.save().
     */
    Product save(Product product);

    /**
     * SAP Commerce:
     * Retrieve ProductModel by PK.
     *
     * Spring Boot:
     * Delegates to JpaRepository.findById().
     */
    Optional<Product> findById(Long id);

    /**
     * SAP Commerce:
     * Retrieve ProductModel using product code.
     *
     * Spring Boot:
     * Equivalent query is handled by Spring Data JPA.
     */
    Optional<Product> findByCode(String code);

    /**
     * SAP Commerce:
     * Retrieve all products.
     *
     * Spring Boot:
     * Delegates to JpaRepository.findAll().
     */
    List<Product> findAll();

    /**
     * SAP Commerce:
     * Check whether a product with the given code exists.
     *
     * Spring Boot:
     * Delegates to Spring Data JPA existsByCode().
     */
    boolean existsByCode(String code);

    /**
     * SAP Commerce:
     * Delete a ProductModel.
     *
     * Spring Boot:
     * Delegates to JpaRepository.delete().
     */
    void delete(Product product);
}