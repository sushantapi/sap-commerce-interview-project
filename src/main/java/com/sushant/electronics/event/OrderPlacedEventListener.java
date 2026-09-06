package com.sushant.electronics.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class OrderPlacedEventListener {

    private static final Logger log = LoggerFactory.getLogger(OrderPlacedEventListener.class);

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(OrderPlacedEvent event) {
        log.info("Order placed event received. orderId={}, customerId={}",
                event.orderId(), event.customerId());
        log.info("Order confirmation notification triggered for orderId={}", event.orderId());
    }
}
