package com.sushant.electronics.service.impl;

import com.sushant.electronics.dao.ProductDao;
import com.sushant.electronics.entity.Product;
import com.sushant.electronics.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * SAP Commerce:
 * Service Layer implementation.
 *
 * Spring Boot:
 * @Service implementation containing business logic.
 *
 * The Service Layer should not directly access the repository.
 * It communicates with the DAO layer.
 *
 * SAP Commerce flow:
 * Service -> DAO -> FlexibleSearch -> Database
 *
 * Spring Boot flow:
 * Service -> DAO -> Repository/JPA -> Database
 */
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductDao productDao;

    public ProductServiceImpl(ProductDao productDao) {
        this.productDao = productDao;
    }

    /**
     * SAP Commerce:
     * Creates/saves a ProductModel through the Service Layer.
     *
     * Spring Boot:
     * Delegates persistence to ProductDao.
     */
    @Override
    public Product createProduct(Product product) {
        return productDao.save(product);
    }

    /**
     * SAP Commerce:
     * Retrieves ProductModel using its PK.
     *
     * Spring Boot:
     * Delegates lookup to ProductDao.
     */
    @Override
    public Product getProductById(Long id) {
        return productDao.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Product not found with id: " + id));
    }

    /**
     * SAP Commerce:
     * Retrieves ProductModel using product code.
     *
     * Spring Boot:
     * Delegates lookup to ProductDao.
     */
    @Override
    public Product getProductByCode(String code) {
        return productDao.findByCode(code)
                .orElseThrow(() ->
                        new RuntimeException("Product not found with code: " + code));
    }

    /**
     * SAP Commerce:
     * Retrieves products through the Service Layer.
     *
     * Spring Boot:
     * Delegates to ProductDao.
     */
    @Override
    public List<Product> getAllProducts() {
        return productDao.findAll();
    }

    /**
     * SAP Commerce:
     * Updates a ProductModel through the Service Layer.
     *
     * Spring Boot:
     * Retrieves the existing entity through DAO,
     * modifies it, and delegates persistence back to DAO.
     */
    @Override
    public Product updateProduct(Long id, Product product) {

        Product existingProduct = getProductById(id);

        existingProduct.setCode(product.getCode());
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setStock(product.getStock());
        existingProduct.setActive(product.getActive());

        return productDao.save(existingProduct);
    }

    /**
     * SAP Commerce:
     * Removes a ProductModel through the persistence layer.
     *
     * Spring Boot:
     * Delegates deletion to ProductDao.
     */
    @Override
    public void deleteProduct(Long id) {

        Product existingProduct = getProductById(id);

        productDao.delete(existingProduct);
    }
}