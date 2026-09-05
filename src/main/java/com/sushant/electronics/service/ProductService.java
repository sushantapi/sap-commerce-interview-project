package com.sushant.electronics.service;

import com.sushant.electronics.entity.Product;

import java.util.List;


/**
 * SAP Commerce:
 * Service Layer interface.
 *
 * Spring Boot:
 * Service abstraction containing business operations.
 *
 * Business rules should primarily be coordinated in this layer,
 * rather than inside the Controller or DAO.
 */
public interface ProductService {

    Product createProduct(Product product);

    Product getProductById(Long id);

    Product getProductByCode(String code);

    List<Product> getAllProducts();

    Product updateProduct(Long id, Product product);

    void deleteProduct(Long id);
}