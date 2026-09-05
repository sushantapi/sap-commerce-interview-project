package com.sushant.electronics.dto;

import lombok.*;

import java.math.BigDecimal;

/**
 * SAP Commerce:
 * Represents incoming API data.
 *
 * Spring Boot:
 * Request DTO used by the REST Controller.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest {

    private String code;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private Boolean active;
}