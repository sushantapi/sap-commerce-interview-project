package com.sushant.electronics.service;

import com.sushant.electronics.entity.Order;

import java.util.List;

public interface OrderService {
    Order checkout(String customerId);
    Order getOrder(Long orderId);
    List<Order> getOrders(String customerId);
}
