package com.sushant.electronics.search;

import com.sushant.electronics.entity.Product;

public record ProductIndexEvent(Action action, Product product, Long productId) {

    public enum Action {
        INDEX,
        DELETE
    }

    public static ProductIndexEvent index(Product product) {
        return new ProductIndexEvent(Action.INDEX, product, product.getId());
    }

    public static ProductIndexEvent delete(Long productId) {
        return new ProductIndexEvent(Action.DELETE, null, productId);
    }
}
