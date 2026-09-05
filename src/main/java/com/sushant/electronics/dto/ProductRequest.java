package com.sushant.electronics.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest {

    @NotBlank(message = "Product code is required")
    private String code;

    @NotBlank(message = "Product name is required")
    private String name;

    private String description;

    @NotNull(message = "Product price is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Product price must be zero or greater")
    private BigDecimal price;

    @NotNull(message = "Product stock is required")
    @PositiveOrZero(message = "Product stock must be zero or greater")
    private Integer stock;

    @NotNull(message = "Product active flag is required")
    private Boolean active;
}
