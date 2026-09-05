package com.sushant.electronics.service.impl;

import com.sushant.electronics.entity.Cart;
import com.sushant.electronics.entity.CartItem;
import com.sushant.electronics.entity.Order;
import com.sushant.electronics.entity.OrderItem;
import com.sushant.electronics.entity.OrderStatus;
import com.sushant.electronics.exception.CartException;
import com.sushant.electronics.exception.OrderException;
import com.sushant.electronics.repository.CartRepository;
import com.sushant.electronics.repository.OrderRepository;
import com.sushant.electronics.repository.ProductRepository;
import com.sushant.electronics.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public OrderServiceImpl(OrderRepository orderRepository,
                            CartRepository cartRepository,
                            ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public Order checkout(String customerId) {
        Cart cart = cartRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new CartException("Cart not found for customer: " + customerId));

        if (cart.getItems().isEmpty()) {
            throw new OrderException("Cannot checkout an empty cart");
        }

        Order order = Order.builder()
                .customerId(customerId)
                .status(OrderStatus.PLACED)
                .createdAt(LocalDateTime.now())
                .totalAmount(BigDecimal.ZERO)
                .build();

        BigDecimal total = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getItems()) {
            var product = cartItem.getProduct();

            if (!Boolean.TRUE.equals(product.getActive())) {
                throw new OrderException("Product is inactive: " + product.getCode());
            }

            int updatedRows = productRepository.decreaseStockIfAvailable(
                    product.getId(), cartItem.getQuantity());

            if (updatedRows != 1) {
                throw new OrderException("Insufficient stock for product: " + product.getCode());
            }

            BigDecimal unitPrice = product.getPrice();
            BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(cartItem.getQuantity()));

            order.addItem(OrderItem.builder()
                    .productId(product.getId())
                    .productCode(product.getCode())
                    .productName(product.getName())
                    .unitPrice(unitPrice)
                    .quantity(cartItem.getQuantity())
                    .lineTotal(lineTotal)
                    .build());

            total = total.add(lineTotal);
        }

        order.setTotalAmount(total);
        Order savedOrder = orderRepository.save(order);

        cart.getItems().clear();
        cartRepository.save(cart);

        return savedOrder;
    }

    @Override
    @Transactional(readOnly = true)
    public Order getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException("Order not found: " + orderId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getOrders(String customerId) {
        return orderRepository.findByCustomerIdOrderByCreatedAtDesc(customerId);
    }
}
