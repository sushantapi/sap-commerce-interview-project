package com.sushant.electronics.job;

import com.sushant.electronics.search.SolrProductSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * SAP Commerce equivalent:
 * CronJob for scheduled product/Solr maintenance.
 *
 * Spring Boot implementation:
 * @Scheduled invokes the job according to the configured cron expression.
 */
@Component
public class ProductSolrReindexJob {

    private static final Logger log = LoggerFactory.getLogger(ProductSolrReindexJob.class);

    private final SolrProductSearchService solrProductSearchService;

    public ProductSolrReindexJob(SolrProductSearchService solrProductSearchService) {
        this.solrProductSearchService = solrProductSearchService;
    }

    @Scheduled(cron = "${jobs.product-solr-reindex.cron:0 0 2 * * *}")
    public void reindexProducts() {
        log.info("Starting scheduled product Solr reindex job");
        int indexedProducts = solrProductSearchService.reindexAll();
        log.info("Completed scheduled product Solr reindex job. Indexed products: {}", indexedProducts);
    }
}
