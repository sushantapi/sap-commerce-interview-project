package com.sushant.electronics.search;

import com.sushant.electronics.dto.ProductData;
import com.sushant.electronics.entity.Product;
import com.sushant.electronics.mapper.ProductMapper;
import com.sushant.electronics.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Solr-backed product search and indexing.
 *
 * SAP Commerce equivalent concepts:
 * - ProductModel -> indexed product document
 * - Indexed properties -> Solr document fields
 * - Full index -> reindexAll()
 * - Incremental index -> indexProduct()/deleteProduct()
 */
@Service
public class SolrProductSearchService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final JsonMapper jsonMapper;
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final String solrUrl;

    public SolrProductSearchService(
            ProductRepository productRepository,
            ProductMapper productMapper,
            JsonMapper jsonMapper,
            @Value("${solr.url:http://localhost:8983/solr/products}") String solrUrl) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.jsonMapper = jsonMapper;
        this.solrUrl = solrUrl;
    }

    public void indexProduct(Product product) {
        sendUpdate("/update/json/docs?commit=true", document(product));
    }

    public void deleteProduct(Long productId) {
        Map<String, Object> request = Map.of(
                "delete", Map.of("id", String.valueOf(productId)));
        sendUpdate("/update?commit=true", request);
    }

    public int reindexAll() {
        sendUpdate("/update?commit=true", Map.of("delete", Map.of("query", "*:*")));

        List<Map<String, Object>> documents = productRepository.findAll().stream()
                .map(this::document)
                .toList();

        if (!documents.isEmpty()) {
            sendUpdate("/update/json/docs?commit=true", documents);
        }
        return documents.size();
    }

    public Page<ProductData> search(String query, Pageable pageable) {
        int start = pageable.getPageNumber() * pageable.getPageSize();
        int rows = pageable.getPageSize();
        String solrQuery = buildQuery(query);
        String sort = buildSort(pageable);

        String url = solrUrl + "/select?q=" + encode(solrQuery)
                + "&start=" + start
                + "&rows=" + rows
                + "&sort=" + encode(sort)
                + "&wt=json";

        try {
            HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                    .GET()
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            ensureSuccess(response);

            JsonNode root = jsonMapper.readTree(response.body());
            JsonNode result = root.path("response");
            long total = result.path("numFound").asLong();
            List<ProductData> products = new ArrayList<>();

            for (JsonNode doc : result.path("docs")) {
                products.add(toProductData(doc));
            }

            return new PageImpl<>(products, pageable, total);
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to search products in Solr", ex);
        }
    }

    private Map<String, Object> document(Product product) {
        Map<String, Object> document = new LinkedHashMap<>();
        document.put("id", String.valueOf(product.getId()));
        document.put("code", product.getCode());
        document.put("name", product.getName());
        document.put("name_sort", product.getName());
        document.put("description", product.getDescription());
        document.put("price", product.getPrice());
        document.put("stock", product.getStock());
        document.put("active", product.getActive());
        return document;
    }

    private ProductData toProductData(JsonNode doc) {
        return ProductData.builder()
                .id(doc.path("id").asLong())
                .code(doc.path("code").asText())
                .name(doc.path("name").asText())
                .description(doc.path("description").isMissingNode() ? null : doc.path("description").asText())
                .price(doc.path("price").isMissingNode() ? null : new BigDecimal(doc.path("price").asText()))
                .stock(doc.path("stock").isMissingNode() ? null : doc.path("stock").asInt())
                .active(doc.path("active").isMissingNode() ? null : doc.path("active").asBoolean())
                .build();
    }

    private void sendUpdate(String path, Object body) {
        try {
            String json = jsonMapper.writeValueAsString(body);
            HttpRequest request = HttpRequest.newBuilder(URI.create(solrUrl + path))
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            ensureSuccess(response);
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to update Solr index. Is Solr running?", ex);
        }
    }

    private void ensureSuccess(HttpResponse<String> response) {
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IllegalStateException("Solr returned HTTP " + response.statusCode() + ": " + response.body());
        }
    }

    private String buildQuery(String query) {
        if (query == null || query.isBlank()) {
            return "*:*";
        }
        String value = escapeQuery(query.trim());
        return "code:*" + value + "* OR name:*" + value + "* OR description:*" + value + "*";
    }

    private String buildSort(Pageable pageable) {
        if (pageable.getSort().isUnsorted()) {
            return "name_sort asc";
        }

        String property = pageable.getSort().stream().findFirst().map(order -> switch (order.getProperty()) {
            case "name" -> "name_sort";
            case "price" -> "price";
            case "code" -> "code";
            default -> "name_sort";
        }).orElse("name_sort");

        String direction = pageable.getSort().stream().findFirst()
                .map(order -> order.isAscending() ? "asc" : "desc")
                .orElse("asc");

        return property + " " + direction;
    }

    private String escapeQuery(String value) {
        return value.replace("\\", "\\\\")
                .replace("+", "\\+")
                .replace("-", "\\-")
                .replace("&&", "\\&&")
                .replace("||", "\\||")
                .replace("!", "\\!")
                .replace("(", "\\(")
                .replace(")", "\\)")
                .replace("{", "\\{")
                .replace("}", "\\}")
                .replace("[", "\\[")
                .replace("]", "\\]")
                .replace("^", "\\^")
                .replace("\"", "\\\"")
                .replace("~", "\\~")
                .replace("?", "\\?")
                .replace(":", "\\:")
                .replace("/", "\\/")
                .replace(" ", "\\ ");
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
