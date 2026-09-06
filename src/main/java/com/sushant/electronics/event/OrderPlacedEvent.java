package com.sushant.electronics.event;

public record OrderPlacedEvent(Long orderId, String customerId) {
}
