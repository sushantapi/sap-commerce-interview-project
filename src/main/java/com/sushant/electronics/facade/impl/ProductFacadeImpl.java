package com.sushant.electronics.facade.impl;

import com.sushant.electronics.cache.ProductCacheService;
import com.sushant.electronics.converter.ProductConverter;
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
 * - Convert Product entities into ProductData through Converter/Populator.
 * - Convert ProductRequest into Product entities.
 * - Use Redis as a read-through cache for single-product lookups.
 */
@Service
public class ProductFacadeImpl implements ProductFacade {

    private final ProductService productService;
    private final ProductMapper productMapper;
    private final ProductConverter productConverter;
    private final ProductCacheService productCacheService;

    public ProductFacadeImpl(
            ProductService productService,
            ProductMapper productMapper,
            ProductConverter productConverter,
            ProductCacheService productCacheService) {

        this.productService = productService;
        this.productMapper = productMapper;
        this.productConverter = productConverter;
        this.productCacheService = productCacheService;
    }

    @Override
    public ProductData createProduct(ProductRequest productRequest) {
        Product product = productMapper.toEntity(productRequest);
        Product savedProduct = productService.createProduct(product);
        productCacheService.put(savedProduct);
        return productConverter.convert(savedProduct);
    }

    @Override
    public ProductData getProductById(Long id) {
        return productCacheService.getById(id)
                .orElseGet(() -> {
                    Product product = productService.getProductById(id);
                    productCacheService.put(product);
                    return productConverter.convert(product);
                });
    }

    @Override
    public ProductData getProductByCode(String code) {
        return productCacheService.getByCode(code)
                .orElseGet(() -> {
                    Product product = productService.getProductByCode(code);
                    productCacheService.put(product);
                    return productConverter.convert(product);
                });
    }

    @Override
    public List<ProductData> getAllProducts() {
        return productService.getAllProducts()
                .stream()
                .map(productConverter::convert)
                .toList();
    }

    @Override
    public Page<ProductData> searchProducts(String query, Pageable pageable) {
        return productService.searchProducts(query, pageable)
                .map(productConverter::convert);
    }

    @Override
    public ProductData updateProduct(Long id, ProductRequest productRequest) {
        Product existingProduct = productService.getProductById(id);
        productCacheService.evict(existingProduct);

        Product product = productMapper.toEntity(productRequest);
        Product updatedProduct = productService.updateProduct(id, product);
        productCacheService.put(updatedProduct);
        return productConverter.convert(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        Product existingProduct = productService.getProductById(id);
        productCacheService.evict(existingProduct);
        productService.deleteProduct(id);
    }
}
