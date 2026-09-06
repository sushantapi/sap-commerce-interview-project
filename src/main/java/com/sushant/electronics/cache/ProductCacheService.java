package com.sushant.electronics.cache;

import com.sushant.electronics.dto.ProductData;
import com.sushant.electronics.mapper.ProductMapper;
import com.sushant.electronics.entity.Product;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.json.JsonMapper;

import java.time.Duration;
import java.util.Optional;

/**
 * Redis cache for frequently accessed product data.
 *
 * Cache keys are deliberately separated by lookup type so a product can be
 * retrieved efficiently by either id or code without maintaining duplicate
 * application-side maps.
 */
@Service
public class ProductCacheService {

    private static final Duration TTL = Duration.ofMinutes(10);
    private static final String ID_PREFIX = "product:id:";
    private static final String CODE_PREFIX = "product:code:";

    private final StringRedisTemplate redisTemplate;
    private final JsonMapper jsonMapper;
    private final ProductMapper productMapper;

    public ProductCacheService(StringRedisTemplate redisTemplate,
                               JsonMapper jsonMapper,
                               ProductMapper productMapper) {
        this.redisTemplate = redisTemplate;
        this.jsonMapper = jsonMapper;
        this.productMapper = productMapper;
    }

    public Optional<ProductData> getById(Long id) {
        return read(ID_PREFIX + id);
    }

    public Optional<ProductData> getByCode(String code) {
        return read(CODE_PREFIX + code);
    }

    public void put(Product product) {
        ProductData data = productMapper.toData(product);
        write(ID_PREFIX + product.getId(), data);
        if (product.getCode() != null) {
            write(CODE_PREFIX + product.getCode(), data);
        }
    }

    public void evict(Product product) {
        redisTemplate.delete(ID_PREFIX + product.getId());
        if (product.getCode() != null) {
            redisTemplate.delete(CODE_PREFIX + product.getCode());
        }
    }

    public void evictById(Long id) {
        redisTemplate.delete(ID_PREFIX + id);
    }

    private Optional<ProductData> read(String key) {
        try {
            String json = redisTemplate.opsForValue().get(key);
            if (json == null) {
                return Optional.empty();
            }
            return Optional.of(jsonMapper.readValue(json, ProductData.class));
        } catch (Exception ex) {
            // Cache failures must not make the product API unavailable.
            redisTemplate.delete(key);
            return Optional.empty();
        }
    }

    private void write(String key, ProductData data) {
        try {
            redisTemplate.opsForValue().set(
                    key,
                    jsonMapper.writeValueAsString(data),
                    TTL);
        } catch (Exception ignored) {
            // Redis is an optimization. The database remains the source of truth.
        }
    }
}
