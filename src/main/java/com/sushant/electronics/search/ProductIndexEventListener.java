package com.sushant.electronics.search;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class ProductIndexEventListener {

    private final SolrProductSearchService searchService;

    public ProductIndexEventListener(SolrProductSearchService searchService) {
        this.searchService = searchService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(ProductIndexEvent event) {
        if (event.action() == ProductIndexEvent.Action.INDEX) {
            searchService.indexProduct(event.product());
        } else {
            searchService.deleteProduct(event.productId());
        }
    }
}
