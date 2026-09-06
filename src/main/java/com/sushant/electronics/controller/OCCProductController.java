package com.sushant.electronics.controller;

import com.sushant.electronics.dto.ProductData;
import com.sushant.electronics.dto.ProductRequest;
import com.sushant.electronics.facade.ProductFacade;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * OCC-style REST API used to demonstrate SAP Commerce OCC architecture.
 *
 * This is a Spring Boot implementation for interview practice, not the actual
 * SAP Commerce OCC extension framework.
 */
@RestController
@RequestMapping("/occ/v1/products")
public class OCCProductController {

    private final ProductFacade productFacade;

    public OCCProductController(ProductFacade productFacade) {
        this.productFacade = productFacade;
    }

    @GetMapping
    public ResponseEntity<List<ProductData>> getProducts() {
        return ResponseEntity.ok(productFacade.getAllProducts());
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProductData>> searchProducts(
            @RequestParam(defaultValue = "") String query,
            @PageableDefault(size = 20, sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return ResponseEntity.ok(productFacade.searchProducts(query, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductData> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productFacade.getProductById(id));
    }

    @PostMapping
    public ResponseEntity<ProductData> createProduct(
            @Valid @RequestBody ProductRequest productRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productFacade.createProduct(productRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductData> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest productRequest) {
        return ResponseEntity.ok(productFacade.updateProduct(id, productRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productFacade.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
