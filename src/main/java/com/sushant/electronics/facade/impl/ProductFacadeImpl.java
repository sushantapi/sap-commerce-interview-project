package com.sushant.electronics.facade.impl;

import com.sushant.electronics.cache.ProductCacheService;
import com.sushant.electronics.dto.ProductData;
import com.sushant.electronics.dto.ProductRequest;
import com.sushant.electronics.entity.Product;
import com.sushant.electronics.facade.ProductFacade;
import com.sushant.electronics.mapper.ProductMapper;
import com.sushant.electronics.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * SAP Commerce:
 * Facade implementation.
 *
 * Spring Boot:
 * @Service implementation of ProductFacade.
 *
 * Responsibility:
 * - Coordinate the Service Layer.
 * - Convert Product entities into ProductData.
 * - Convert ProductRequest into Product entities.
 * - Use Redis as a read-through cache for single-product lookups.
 *
 * SAP Commerce concept:
 * OCC Controller -> Facade -> Service
 */
@Service
public class ProductFacadeImpl implements ProductFacade {

    private final ProductService productService;
    private final ProductMapper productMapper;
    private final ProductCacheService productCacheService;

    public ProductFacadeImpl(
            ProductService productService,
            ProductMapper productMapper,
            ProductCacheService productCacheService) {

        this.productService = productService;
        this.productMapper = productMapper;
        this.productCacheService = productCacheService;
    }

    @Override
    public ProductData createProduct(ProductRequest productRequest) {
        Product product = productMapper.toEntity(productRequest);
        Product savedProduct = productService.createProduct(product);
        productCacheService.put(savedProduct);
        return productMapper.toData(savedProduct);
    }

    @Override
    public ProductData getProductById(Long id) {
        return productCacheService.getById(id)
                .orElseGet(() -> {
                    Product product = productService.getProductById(id);
                    productCacheService.put(product);
                    return productMapper.toData(product);
                });
    }

    @Override
    public ProductData getProductByCode(String code) {
        return productCacheService.getByCode(code)
                .orElseGet(() -> {
                    Product product = productService.getProductByCode(code);
                    productCacheService.put(product);
                    return productMapper.toData(product);
                });
    }

    @Override
    public List<ProductData> getAllProducts() {
        return productService.getAllProducts()
                .stream()
                .map(productMapper::toData)
                .toList();
    }

    @Override
    public Page<ProductData> searchProducts(String query, Pageable pageable) {
        return productService.searchProducts(query, pageable)
                .map(productMapper::toData);
    }

    @Override
    public ProductData updateProduct(Long id, ProductRequest productRequest) {
        Product existingProduct = productService.getProductById(id);
        productCacheService.evict(existingProduct);

        Product product = productMapper.toEntity(productRequest);
        Product updatedProduct = productService.updateProduct(id, product);
        productCacheService.put(updatedProduct);
        return productMapper.toData(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        Product existingProduct = productService.getProductById(id);
        productCacheService.evict(existingProduct);
        productService.deleteProduct(id);
    }
}
