package com.sushant.electronics.controller;

import com.sushant.electronics.dto.OrderData;
import com.sushant.electronics.facade.OrderFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderFacade orderFacade;

    public OrderController(OrderFacade orderFacade) {
        this.orderFacade = orderFacade;
    }

    @PostMapping("/{customerId}/checkout")
    public ResponseEntity<OrderData> checkout(@PathVariable String customerId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderFacade.checkout(customerId));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderData> getOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderFacade.getOrder(orderId));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderData>> getOrders(@PathVariable String customerId) {
        return ResponseEntity.ok(orderFacade.getOrders(customerId));
    }
}
