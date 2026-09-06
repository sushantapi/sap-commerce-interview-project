package com.sushant.electronics.converter;

import com.sushant.electronics.dto.ProductData;
import com.sushant.electronics.entity.Product;
import org.springframework.stereotype.Component;

/**
 * SAP Commerce equivalent:
 * Converter<ProductModel, ProductData>
 *
 * Responsibility:
 * Create the target DTO and delegate field population to the Populator.
 */
@Component
public class ProductConverter {

    private final ProductPopulator productPopulator;

    public ProductConverter(ProductPopulator productPopulator) {
        this.productPopulator = productPopulator;
    }

    public ProductData convert(Product product) {
        if (product == null) {
            return null;
        }

        ProductData target = new ProductData();
        productPopulator.populate(product, target);
        return target;
    }
}
