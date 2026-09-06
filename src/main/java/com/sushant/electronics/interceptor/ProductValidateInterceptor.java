package com.sushant.electronics.interceptor;

import com.sushant.electronics.entity.Product;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * SAP Commerce equivalent:
 * ValidateInterceptor<ProductModel>
 *
 * Responsibility:
 * Validate model-level business rules before persistence.
 */
@Component
public class ProductValidateInterceptor {

    public void validate(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product must not be null");
        }

        if (product.getCode() == null || product.getCode().isBlank()) {
            throw new IllegalArgumentException("Product code must not be blank");
        }

        if (product.getName() == null || product.getName().isBlank()) {
            throw new IllegalArgumentException("Product name must not be blank");
        }

        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Product price cannot be negative");
        }

        if (product.getStock() == null || product.getStock() < 0) {
            throw new IllegalArgumentException("Product stock cannot be negative");
        }

        if (product.getActive() == null) {
            throw new IllegalArgumentException("Product active flag must not be null");
        }
    }
}
