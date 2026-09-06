package com.sushant.electronics.service.impl;

import com.sushant.electronics.dao.ProductDao;
import com.sushant.electronics.entity.Product;
import com.sushant.electronics.exception.DuplicateProductException;
import com.sushant.electronics.exception.ProductNotFoundException;
import com.sushant.electronics.interceptor.ProductPrepareInterceptor;
import com.sushant.electronics.interceptor.ProductValidateInterceptor;
import com.sushant.electronics.search.ProductIndexEvent;
import com.sushant.electronics.service.ProductService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductDao productDao;
    private final ApplicationEventPublisher eventPublisher;
    private final ProductPrepareInterceptor prepareInterceptor;
    private final ProductValidateInterceptor validateInterceptor;

    public ProductServiceImpl(ProductDao productDao,
                              ApplicationEventPublisher eventPublisher,
                              ProductPrepareInterceptor prepareInterceptor,
                              ProductValidateInterceptor validateInterceptor) {
        this.productDao = productDao;
        this.eventPublisher = eventPublisher;
        this.prepareInterceptor = prepareInterceptor;
        this.validateInterceptor = validateInterceptor;
    }

    @Override
    public Product createProduct(Product product) {
        prepareInterceptor.prepare(product);
        validateInterceptor.validate(product);

        if (productDao.existsByCode(product.getCode())) {
            throw new DuplicateProductException(
                    "Product already exists with code: " + product.getCode());
        }

        Product savedProduct = productDao.save(product);
        eventPublisher.publishEvent(ProductIndexEvent.index(savedProduct));
        return savedProduct;
    }

    @Override
    @Transactional(readOnly = true)
    public Product getProductById(Long id) {
        return productDao.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException("Product not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Product getProductByCode(String code) {
        return productDao.findByCode(code)
                .orElseThrow(() ->
                        new ProductNotFoundException("Product not found with code: " + code));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> searchProducts(String query, Pageable pageable) {
        return productDao.search(query == null ? "" : query.trim(), pageable);
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        prepareInterceptor.prepare(product);
        validateInterceptor.validate(product);

        Product existingProduct = getProductById(id);

        if (!existingProduct.getCode().equals(product.getCode())
                && productDao.existsByCode(product.getCode())) {
            throw new DuplicateProductException(
                    "Product already exists with code: " + product.getCode());
        }

        existingProduct.setCode(product.getCode());
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setStock(product.getStock());
        existingProduct.setActive(product.getActive());

        Product savedProduct = productDao.save(existingProduct);
        eventPublisher.publishEvent(ProductIndexEvent.index(savedProduct));
        return savedProduct;
    }

    @Override
    public void deleteProduct(Long id) {
        Product existingProduct = getProductById(id);
        productDao.delete(existingProduct);
        eventPublisher.publishEvent(ProductIndexEvent.delete(id));
    }
}
