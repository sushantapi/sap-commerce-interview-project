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
 * These endpoints demonstrate equivalent JPA/JPQL patterns; they do not execute
 * the SAP Commerce FlexibleSearch engine.
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

    @GetMapping("/name")
    public List<Product> findByName(@RequestParam String name) {
        return productDao.findByName(name);
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

    @GetMapping("/category/{categoryCode}")
    public List<Product> findByCategoryCode(@PathVariable String categoryCode) {
        return productDao.findByCategoryCode(categoryCode);
    }

    @GetMapping("/active/page")
    public List<Product> findActiveProductsPage(
            @RequestParam BigDecimal minimumPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (page < 0 || size < 1 || size > 100) {
            throw new IllegalArgumentException("page must be >= 0 and size must be between 1 and 100");
        }
        return productDao.findActiveProductsPage(minimumPrice, page, size);
    }

    @GetMapping("/active/count")
    public long countActiveProducts(@RequestParam BigDecimal minimumPrice) {
        return productDao.countActiveProducts(minimumPrice);
    }
}
