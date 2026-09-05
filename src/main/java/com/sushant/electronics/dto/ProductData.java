package com.sushant.electronics.dto;

import lombok.*;

import java.math.BigDecimal;

/**
 * SAP Commerce:
 * Similar to ProductData used by the Facade/OCC layer.
 *
 * Spring Boot:
 * DTO used to expose product information outside the persistence layer.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductData {

    private Long id;
    private String code;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private Boolean active;
}