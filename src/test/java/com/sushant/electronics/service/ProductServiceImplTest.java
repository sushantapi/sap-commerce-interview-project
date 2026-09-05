package com.sushant.electronics.service;

import com.sushant.electronics.dao.ProductDao;
import com.sushant.electronics.entity.Product;
import com.sushant.electronics.exception.DuplicateProductException;
import com.sushant.electronics.exception.ProductNotFoundException;
import com.sushant.electronics.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .id(1L)
                .code("IPHONE-15")
                .name("iPhone 15")
                .description("Apple smartphone")
                .price(new BigDecimal("69999.00"))
                .stock(25)
                .active(true)
                .build();
    }

    @Test
    void createProduct_shouldSaveProduct_whenCodeDoesNotExist() {
        when(productDao.existsByCode("IPHONE-15")).thenReturn(false);
        when(productDao.save(product)).thenReturn(product);

        Product result = productService.createProduct(product);

        assertSame(product, result);
        verify(productDao).existsByCode("IPHONE-15");
        verify(productDao).save(product);
    }

    @Test
    void createProduct_shouldThrowDuplicateException_whenCodeExists() {
        when(productDao.existsByCode("IPHONE-15")).thenReturn(true);

        assertThrows(DuplicateProductException.class,
                () -> productService.createProduct(product));

        verify(productDao).existsByCode("IPHONE-15");
        verify(productDao, never()).save(any(Product.class));
    }

    @Test
    void getProductById_shouldReturnProduct_whenProductExists() {
        when(productDao.findById(1L)).thenReturn(Optional.of(product));

        Product result = productService.getProductById(1L);

        assertEquals(product, result);
        verify(productDao).findById(1L);
    }

    @Test
    void getProductById_shouldThrowNotFound_whenProductDoesNotExist() {
        when(productDao.findById(999L)).thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(
                ProductNotFoundException.class,
                () -> productService.getProductById(999L));

        assertEquals("Product not found with id: 999", exception.getMessage());
        verify(productDao).findById(999L);
    }

    @Test
    void getProductByCode_shouldReturnProduct_whenProductExists() {
        when(productDao.findByCode("IPHONE-15")).thenReturn(Optional.of(product));

        Product result = productService.getProductByCode("IPHONE-15");

        assertEquals(product, result);
        verify(productDao).findByCode("IPHONE-15");
    }

    @Test
    void getAllProducts_shouldReturnProducts() {
        when(productDao.findAll()).thenReturn(List.of(product));

        List<Product> result = productService.getAllProducts();

        assertEquals(1, result.size());
        assertEquals("IPHONE-15", result.get(0).getCode());
        verify(productDao).findAll();
    }

    @Test
    void updateProduct_shouldUpdateExistingProduct() {
        Product update = Product.builder()
                .code("IPHONE-15")
                .name("iPhone 15 Updated")
                .description("Updated description")
                .price(new BigDecimal("67999.00"))
                .stock(30)
                .active(true)
                .build();

        when(productDao.findById(1L)).thenReturn(Optional.of(product));
        when(productDao.save(product)).thenReturn(product);

        Product result = productService.updateProduct(1L, update);

        assertEquals("iPhone 15 Updated", result.getName());
        assertEquals(new BigDecimal("67999.00"), result.getPrice());
        assertEquals(30, result.getStock());
        verify(productDao).save(product);
    }

    @Test
    void updateProduct_shouldThrowDuplicateException_whenNewCodeAlreadyExists() {
        Product update = Product.builder()
                .code("SAMSUNG-S24")
                .name("Samsung S24")
                .price(new BigDecimal("79999.00"))
                .stock(10)
                .active(true)
                .build();

        when(productDao.findById(1L)).thenReturn(Optional.of(product));
        when(productDao.existsByCode("SAMSUNG-S24")).thenReturn(true);

        assertThrows(DuplicateProductException.class,
                () -> productService.updateProduct(1L, update));

        verify(productDao, never()).save(any(Product.class));
    }

    @Test
    void deleteProduct_shouldDeleteExistingProduct() {
        when(productDao.findById(1L)).thenReturn(Optional.of(product));

        productService.deleteProduct(1L);

        verify(productDao).delete(product);
    }

    @Test
    void deleteProduct_shouldThrowNotFound_whenProductDoesNotExist() {
        when(productDao.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class,
                () -> productService.deleteProduct(999L));

        verify(productDao, never()).delete(any(Product.class));
    }
}
