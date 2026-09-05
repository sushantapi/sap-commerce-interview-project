package com.sushant.electronics.service.impl;

import com.sushant.electronics.entity.Cart;
import com.sushant.electronics.entity.CartItem;
import com.sushant.electronics.entity.Product;
import com.sushant.electronics.exception.CartException;
import com.sushant.electronics.repository.CartRepository;
import com.sushant.electronics.repository.ProductRepository;
import com.sushant.electronics.service.CartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartServiceImpl(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Cart getOrCreateCart(String customerId) {
        return cartRepository.findByCustomerId(customerId)
                .orElseGet(() -> cartRepository.save(Cart.builder().customerId(customerId).build()));
    }

    @Override
    public Cart addItem(String customerId, Long productId, Integer quantity) {
        Cart cart = getOrCreateCart(customerId);
        Product product = getProduct(productId);

        if (!Boolean.TRUE.equals(product.getActive())) {
            throw new CartException("Product is inactive: " + product.getCode());
        }

        CartItem existing = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        int newQuantity = quantity + (existing == null ? 0 : existing.getQuantity());
        if (newQuantity > product.getStock()) {
            throw new CartException("Insufficient stock for product: " + product.getCode());
        }

        if (existing == null) {
            cart.addItem(CartItem.builder().product(product).quantity(quantity).build());
        } else {
            existing.setQuantity(newQuantity);
        }

        return cartRepository.save(cart);
    }

    @Override
    public Cart updateItem(String customerId, Long productId, Integer quantity) {
        Cart cart = getExistingCart(customerId);
        Product product = getProduct(productId);
        CartItem item = findItem(cart, productId);

        if (quantity > product.getStock()) {
            throw new CartException("Insufficient stock for product: " + product.getCode());
        }

        item.setQuantity(quantity);
        return cartRepository.save(cart);
    }

    @Override
    public Cart removeItem(String customerId, Long productId) {
        Cart cart = getExistingCart(customerId);
        CartItem item = findItem(cart, productId);
        cart.removeItem(item);
        return cartRepository.save(cart);
    }

    private Cart getExistingCart(String customerId) {
        return cartRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new CartException("Cart not found for customer: " + customerId));
    }

    private Product getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new CartException("Product not found: " + productId));
    }

    private CartItem findItem(Cart cart, Long productId) {
        return cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new CartException("Product is not in the cart: " + productId));
    }
}
