package com.sushant.electronics.mapper;

import com.sushant.electronics.dto.ProductData;
import com.sushant.electronics.dto.ProductRequest;
import com.sushant.electronics.entity.Product;
import org.springframework.stereotype.Component;

/**
 * SAP Commerce:
 * Conceptually similar to a Converter + Populator.
 *
 * Spring Boot:
 * Mapper responsible for converting between Entity and DTO/Data objects.
 *
 * Main responsibilities:
 * - Convert ProductRequest -> Product
 * - Convert Product -> ProductData
 *
 * This keeps the persistence model separate from the API model.
 */
@Component
public class ProductMapper {

    /**
     * SAP Commerce:
     * Converts incoming OCC request data into a model/data representation.
     *
     * Spring Boot:
     * Converts ProductRequest DTO into the JPA Product entity.
     */
    public Product toEntity(ProductRequest request) {

        if (request == null) {
            return null;
        }

        return Product.builder()
                .code(request.getCode())
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stock(request.getStock())
                .active(request.getActive())
                .build();
    }

    /**
     * SAP Commerce:
     * Converter/Populator prepares ProductData for the OCC layer.
     *
     * Spring Boot:
     * Converts the JPA Product entity into ProductData DTO.
     */
    public ProductData toData(Product product) {

        if (product == null) {
            return null;
        }

        return ProductData.builder()
                .id(product.getId())
                .code(product.getCode())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .active(product.getActive())
                .build();
    }
}