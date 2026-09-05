package com.sushant.electronics.facade;

import com.sushant.electronics.dto.OrderData;

import java.util.List;

public interface OrderFacade {
    OrderData checkout(String customerId);
    OrderData getOrder(Long orderId);
    List<OrderData> getOrders(String customerId);
}
