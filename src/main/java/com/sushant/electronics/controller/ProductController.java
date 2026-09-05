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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductFacade productFacade;

    public ProductController(ProductFacade productFacade) {
        this.productFacade = productFacade;
    }

    @PostMapping
    public ResponseEntity<ProductData> createProduct(
            @Valid @RequestBody ProductRequest productRequest) {
        ProductData productData = productFacade.createProduct(productRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(productData);
    }

    @GetMapping
    public ResponseEntity<List<ProductData>> getAllProducts() {
        return ResponseEntity.ok(productFacade.getAllProducts());
    }

    /**
     * Product catalog search with database-level pagination and sorting.
     *
     * Examples:
     * GET /api/products/search?query=iphone&page=0&size=10&sort=name,asc
     * GET /api/products/search?page=0&size=20&sort=price,desc
     */
    @GetMapping("/search")
    public ResponseEntity<Page<ProductData>> searchProducts(
            @RequestParam(defaultValue = "") String query,
            @PageableDefault(size = 20, sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return ResponseEntity.ok(productFacade.searchProducts(query, pageable));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<ProductData> getProductByCode(@PathVariable String code) {
        return ResponseEntity.ok(productFacade.getProductByCode(code));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductData> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productFacade.getProductById(id));
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
