package com.sushant.electronics.facade.impl;

import com.sushant.electronics.dto.CartData;
import com.sushant.electronics.dto.CartItemData;
import com.sushant.electronics.dto.CartItemRequest;
import com.sushant.electronics.entity.Cart;
import com.sushant.electronics.entity.CartItem;
import com.sushant.electronics.facade.CartFacade;
import com.sushant.electronics.service.CartService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Component
public class CartFacadeImpl implements CartFacade {

    private final CartService cartService;

    public CartFacadeImpl(CartService cartService) {
        this.cartService = cartService;
    }

    @Override
    @Transactional
    public CartData getCart(String customerId) {
        return toData(cartService.getOrCreateCart(customerId));
    }

    @Override
    @Transactional
    public CartData addItem(String customerId, CartItemRequest request) {
        return toData(cartService.addItem(customerId, request.getProductId(), request.getQuantity()));
    }

    @Override
    @Transactional
    public CartData updateItem(String customerId, Long productId, CartItemRequest request) {
        if (!productId.equals(request.getProductId())) {
            throw new IllegalArgumentException("Path productId and request productId must match");
        }
        return toData(cartService.updateItem(customerId, productId, request.getQuantity()));
    }

    @Override
    @Transactional
    public CartData removeItem(String customerId, Long productId) {
        return toData(cartService.removeItem(customerId, productId));
    }

    private CartData toData(Cart cart) {
        List<CartItemData> items = cart.getItems().stream().map(this::toItemData).toList();
        int totalItems = items.stream().mapToInt(CartItemData::getQuantity).sum();
        BigDecimal totalAmount = items.stream()
                .map(CartItemData::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return CartData.builder()
                .id(cart.getId())
                .customerId(cart.getCustomerId())
                .totalItems(totalItems)
                .totalAmount(totalAmount)
                .items(items)
                .build();
    }

    private CartItemData toItemData(CartItem item) {
        BigDecimal unitPrice = item.getProduct().getPrice();
        BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));

        return CartItemData.builder()
                .id(item.getId())
                .productId(item.getProduct().getId())
                .productCode(item.getProduct().getCode())
                .productName(item.getProduct().getName())
                .unitPrice(unitPrice)
                .quantity(item.getQuantity())
                .lineTotal(lineTotal)
                .build();
    }
}
