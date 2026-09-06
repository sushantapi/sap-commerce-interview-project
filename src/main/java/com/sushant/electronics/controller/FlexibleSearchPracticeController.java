package com.sushant.electronics.controller;

import com.sushant.electronics.dao.impl.ProductFlexibleSearchDao;
import com.sushant.electronics.entity.Product;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/**
 * Interview-practice endpoints for FlexibleSearch concepts.
 *
 * These endpoints are not claiming to execute SAP Commerce FlexibleSearch.
 * They expose the equivalent DAO patterns using JPA/JPQL in this project.
 */
@RestController
@RequestMapping("/api/products/flexible-search")
public class FlexibleSearchPracticeController {

    private final ProductFlexibleSearchDao productDao;

    public FlexibleSearchPracticeController(ProductFlexibleSearchDao productDao) {
        this.productDao = productDao;
    }

    @GetMapping("/code/{code}")
    public Product findByCode(@PathVariable String code) {
        return productDao.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Product not found with code: " + code));
    }

    @GetMapping("/active")
    public List<Product> findActiveProductsByMinimumPrice(
            @RequestParam BigDecimal minimumPrice) {
        return productDao.findActiveProductsByMinimumPrice(minimumPrice);
    }

    @GetMapping("/search")
    public List<Product> search(@RequestParam String query) {
        return productDao.searchByNameOrCode(query);
    }
}
