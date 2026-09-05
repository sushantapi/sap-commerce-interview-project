package com.sushant.electronics.facade.impl;

import com.sushant.electronics.dto.OrderData;
import com.sushant.electronics.dto.OrderItemData;
import com.sushant.electronics.entity.Order;
import com.sushant.electronics.entity.OrderItem;
import com.sushant.electronics.facade.OrderFacade;
import com.sushant.electronics.service.OrderService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class OrderFacadeImpl implements OrderFacade {

    private final OrderService orderService;

    public OrderFacadeImpl(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    @Transactional
    public OrderData checkout(String customerId) {
        return toData(orderService.checkout(customerId));
    }

    @Override
    @Transactional(readOnly = true)
    public OrderData getOrder(Long orderId) {
        return toData(orderService.getOrder(orderId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderData> getOrders(String customerId) {
        return orderService.getOrders(customerId).stream().map(this::toData).toList();
    }

    private OrderData toData(Order order) {
        List<OrderItemData> items = order.getItems().stream().map(this::toItemData).toList();
        return OrderData.builder()
                .id(order.getId())
                .customerId(order.getCustomerId())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .createdAt(order.getCreatedAt())
                .items(items)
                .build();
    }

    private OrderItemData toItemData(OrderItem item) {
        return OrderItemData.builder()
                .id(item.getId())
                .productId(item.getProductId())
                .productCode(item.getProductCode())
                .productName(item.getProductName())
                .unitPrice(item.getUnitPrice())
                .quantity(item.getQuantity())
                .lineTotal(item.getLineTotal())
                .build();
    }
}
