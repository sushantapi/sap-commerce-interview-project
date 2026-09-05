package com.sushant.electronics.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartData {

    private Long id;
    private String customerId;
    private Integer totalItems;
    private BigDecimal totalAmount;
    private List<CartItemData> items;
}
