package com.sushant.electronics.controller;

import com.sushant.electronics.dto.CartData;
import com.sushant.electronics.dto.CartItemRequest;
import com.sushant.electronics.facade.CartFacade;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartFacade cartFacade;

    public CartController(CartFacade cartFacade) {
        this.cartFacade = cartFacade;
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CartData> getCart(@PathVariable String customerId) {
        return ResponseEntity.ok(cartFacade.getCart(customerId));
    }

    @PostMapping("/{customerId}/items")
    public ResponseEntity<CartData> addItem(
            @PathVariable String customerId,
            @Valid @RequestBody CartItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cartFacade.addItem(customerId, request));
    }

    @PutMapping("/{customerId}/items/{productId}")
    public ResponseEntity<CartData> updateItem(
            @PathVariable String customerId,
            @PathVariable Long productId,
            @Valid @RequestBody CartItemRequest request) {
        return ResponseEntity.ok(cartFacade.updateItem(customerId, productId, request));
    }

    @DeleteMapping("/{customerId}/items/{productId}")
    public ResponseEntity<CartData> removeItem(
            @PathVariable String customerId,
            @PathVariable Long productId) {
        return ResponseEntity.ok(cartFacade.removeItem(customerId, productId));
    }
}
