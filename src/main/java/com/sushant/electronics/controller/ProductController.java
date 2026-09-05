package com.sushant.electronics.controller;

import com.sushant.electronics.dto.ProductData;
import com.sushant.electronics.dto.ProductRequest;
import com.sushant.electronics.facade.ProductFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * SAP Commerce:
 * OCC Controller.
 *
 * Spring Boot:
 * REST Controller.
 *
 * Responsibility:
 * - Handle HTTP requests.
 * - Accept request DTOs.
 * - Delegate business operations to the Facade.
 * - Return Data objects as API responses.
 *
 * Important:
 * The Controller should NOT directly call:
 * - Service
 * - DAO
 * - Repository
 *
 * SAP Commerce concept:
 *
 * OCC Controller
 *       ↓
 * Facade
 *       ↓
 * Service
 *       ↓
 * DAO
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductFacade productFacade;

    public ProductController(ProductFacade productFacade) {
        this.productFacade = productFacade;
    }

    /**
     * Create a new product.
     *
     * POST /api/products
     */
    @PostMapping
    public ResponseEntity<ProductData> createProduct(
            @RequestBody ProductRequest productRequest) {

        ProductData productData =
                productFacade.createProduct(productRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productData);
    }

    /**
     * Get all products.
     *
     * GET /api/products
     */
    @GetMapping
    public ResponseEntity<List<ProductData>> getAllProducts() {

        List<ProductData> products =
                productFacade.getAllProducts();

        return ResponseEntity.ok(products);
    }

    /**
     * Get product by ID.
     *
     * GET /api/products/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductData> getProductById(
            @PathVariable Long id) {

        ProductData productData =
                productFacade.getProductById(id);

        return ResponseEntity.ok(productData);
    }

    /**
     * Get product by product code.
     *
     * GET /api/products/code/{code}
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<ProductData> getProductByCode(
            @PathVariable String code) {

        ProductData productData =
                productFacade.getProductByCode(code);

        return ResponseEntity.ok(productData);
    }

    /**
     * Update an existing product.
     *
     * PUT /api/products/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductData> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductRequest productRequest) {

        ProductData productData =
                productFacade.updateProduct(id, productRequest);

        return ResponseEntity.ok(productData);
    }

    /**
     * Delete a product.
     *
     * DELETE /api/products/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long id) {

        productFacade.deleteProduct(id);

        return ResponseEntity.noContent().build();
    }
}