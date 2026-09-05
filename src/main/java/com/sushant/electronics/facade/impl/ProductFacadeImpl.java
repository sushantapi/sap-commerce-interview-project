package com.sushant.electronics.facade.impl;

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
 *
 * SAP Commerce concept:
 * OCC Controller -> Facade -> Service
 */
@Service
public class ProductFacadeImpl implements ProductFacade {

    private final ProductService productService;
    private final ProductMapper productMapper;

    public ProductFacadeImpl(
            ProductService productService,
            ProductMapper productMapper) {

        this.productService = productService;
        this.productMapper = productMapper;
    }

    @Override
    public ProductData createProduct(ProductRequest productRequest) {
        Product product = productMapper.toEntity(productRequest);
        Product savedProduct = productService.createProduct(product);
        return productMapper.toData(savedProduct);
    }

    @Override
    public ProductData getProductById(Long id) {
        Product product = productService.getProductById(id);
        return productMapper.toData(product);
    }

    @Override
    public ProductData getProductByCode(String code) {
        Product product = productService.getProductByCode(code);
        return productMapper.toData(product);
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
        Product product = productMapper.toEntity(productRequest);
        Product updatedProduct = productService.updateProduct(id, product);
        return productMapper.toData(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        productService.deleteProduct(id);
    }
}
