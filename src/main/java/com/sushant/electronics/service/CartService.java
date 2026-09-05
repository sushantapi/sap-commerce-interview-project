package com.sushant.electronics.service;

import com.sushant.electronics.entity.Cart;

public interface CartService {

    Cart getOrCreateCart(String customerId);

    Cart addItem(String customerId, Long productId, Integer quantity);

    Cart updateItem(String customerId, Long productId, Integer quantity);

    Cart removeItem(String customerId, Long productId);
}
