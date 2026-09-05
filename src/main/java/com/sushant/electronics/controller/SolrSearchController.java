package com.sushant.electronics.controller;

import com.sushant.electronics.dto.ProductData;
import com.sushant.electronics.search.SolrProductSearchService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/products/solr")
public class SolrSearchController {

    private final SolrProductSearchService searchService;

    public SolrSearchController(SolrProductSearchService searchService) {
        this.searchService = searchService;
    }

    /**
     * Search the Solr product index.
     * Example: /api/products/solr/search?query=iphone&page=0&size=10&sort=price,desc
     */
    @GetMapping("/search")
    public ResponseEntity<Page<ProductData>> search(
            @RequestParam(defaultValue = "") String query,
            @PageableDefault(size = 20, sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return ResponseEntity.ok(searchService.search(query, pageable));
    }

    /**
     * Full indexing: rebuild the Solr product index from PostgreSQL.
     */
    @PostMapping("/index")
    public ResponseEntity<Map<String, Object>> reindexAll() {
        int indexedProducts = searchService.reindexAll();
        return ResponseEntity.ok(Map.of(
                "message", "Product index rebuilt successfully",
                "indexedProducts", indexedProducts));
    }
}
