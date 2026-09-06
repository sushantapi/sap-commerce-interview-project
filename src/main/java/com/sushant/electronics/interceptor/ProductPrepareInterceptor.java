package com.sushant.electronics.interceptor;

import com.sushant.electronics.entity.Product;
import org.springframework.stereotype.Component;

/**
 * SAP Commerce equivalent:
 * PrepareInterceptor<ProductModel>
 *
 * Responsibility:
 * Normalize and prepare the model before persistence.
 */
@Component
public class ProductPrepareInterceptor {

    public void prepare(Product product) {
        if (product == null) {
            return;
        }

        if (product.getCode() != null) {
            product.setCode(product.getCode().trim().toUpperCase());
        }

        if (product.getName() != null) {
            product.setName(product.getName().trim());
        }

        if (product.getDescription() != null) {
            product.setDescription(product.getDescription().trim());
        }

        if (product.getActive() == null) {
            product.setActive(Boolean.TRUE);
        }

        if (product.getStock() == null) {
            product.setStock(0);
        }
    }
}
