package com.sushant.electronics.dao.impl;

import com.sushant.electronics.dao.ProductDao;
import com.sushant.electronics.entity.Product;
import com.sushant.electronics.repository.ProductRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * SAP Commerce:
 * DAO implementation responsible for persistence access.
 *
 * Spring Boot:
 * @Repository implementation that delegates to
 * Spring Data JPA's ProductRepository.
 *
 * In SAP Commerce, this layer would typically execute
 * FlexibleSearch queries.
 *
 * In our Spring Boot project, JPA Repository handles
 * the database interaction.
 */
@Repository
public class ProductDaoImpl implements ProductDao {

    private final ProductRepository productRepository;

    public ProductDaoImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Optional<Product> findByCode(String code) {
        return productRepository.findByCode(code);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public boolean existsByCode(String code) {
        return productRepository.existsByCode(code);
    }

    @Override
    public void delete(Product product) {
        productRepository.delete(product);
    }
}