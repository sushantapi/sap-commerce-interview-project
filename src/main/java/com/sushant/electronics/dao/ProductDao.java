package com.sushant.electronics.dao;

import com.sushant.electronics.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

    Product save(Product product);

    Optional<Product> findById(Long id);

    Optional<Product> findByCode(String code);

    List<Product> findAll();

    boolean existsByCode(String code);

    void delete(Product product);

    /**
     * Search products by code or name with database-level pagination and sorting.
     * SAP Commerce equivalent would typically be a FlexibleSearch query with
     * search conditions and paging handled by the search service.
     */
    Page<Product> search(String query, Pageable pageable);
}
