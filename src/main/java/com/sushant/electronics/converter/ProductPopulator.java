package com.sushant.electronics.converter;

import com.sushant.electronics.dto.ProductData;
import com.sushant.electronics.entity.Product;
import org.springframework.stereotype.Component;

/**
 * SAP Commerce equivalent:
 * Populator<ProductModel, ProductData>
 *
 * Responsibility:
 * Copy data from the source model into an already-created target DTO.
 */
@Component
public class ProductPopulator {

    public void populate(Product product, ProductData target) {
        if (product == null || target == null) {
            return;
        }

        target.setId(product.getId());
        target.setCode(product.getCode());
        target.setName(product.getName());
        target.setDescription(product.getDescription());
        target.setPrice(product.getPrice());
        target.setStock(product.getStock());
        target.setActive(product.getActive());
    }
}
