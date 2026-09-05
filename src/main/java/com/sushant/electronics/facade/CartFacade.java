package com.sushant.electronics.facade;

import com.sushant.electronics.dto.CartData;
import com.sushant.electronics.dto.CartItemRequest;

public interface CartFacade {

    CartData getCart(String customerId);

    CartData addItem(String customerId, CartItemRequest request);

    CartData updateItem(String customerId, Long productId, CartItemRequest request);

    CartData removeItem(String customerId, Long productId);
}
