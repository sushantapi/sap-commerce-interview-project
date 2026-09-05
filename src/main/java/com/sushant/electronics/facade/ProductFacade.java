package com.sushant.electronics.facade;

import com.sushant.electronics.dto.ProductData;
import com.sushant.electronics.dto.ProductRequest;

import java.util.List;

/**
 * SAP Commerce:
 * Facade layer used by OCC Controllers.
 *
 * Spring Boot:
 * API-facing abstraction between the Controller
 * and the Service Layer.
 *
 * Responsibility:
 * - Coordinate service operations.
 * - Convert domain objects into API Data objects.
 * - Keep Controller free from business logic.
 *
 * SAP Commerce concept:
 * Controller -> Facade -> Service
 */
public interface ProductFacade {

    ProductData createProduct(ProductRequest productRequest);

    ProductData getProductById(Long id);

    ProductData getProductByCode(String code);

    List<ProductData> getAllProducts();

    ProductData updateProduct(Long id, ProductRequest productRequest);

    void deleteProduct(Long id);
}