package com.sushant.electronics.controller;

import com.sushant.electronics.dto.ProductData;
import com.sushant.electronics.dto.ProductRequest;
import com.sushant.electronics.exception.DuplicateProductException;
import com.sushant.electronics.exception.ProductNotFoundException;
import com.sushant.electronics.facade.ProductFacade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final JsonMapper objectMapper = new JsonMapper();

    @MockitoBean
    private ProductFacade productFacade;

    @Test
    void createProduct_shouldReturn201() throws Exception {
        ProductRequest request = validRequest("IPHONE-15", "iPhone 15");
        ProductData response = productData(1L, "IPHONE-15", "iPhone 15");

        when(productFacade.createProduct(any(ProductRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.code").value("IPHONE-15"));

        verify(productFacade).createProduct(any(ProductRequest.class));
    }

    @Test
    void createProduct_shouldReturn400_whenRequestIsInvalid() throws Exception {
        ProductRequest request = validRequest("", "");
        request.setPrice(new BigDecimal("-1.00"));
        request.setStock(-1);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation Failed"));

        verifyNoInteractions(productFacade);
    }

    @Test
    void getProductById_shouldReturn200() throws Exception {
        when(productFacade.getProductById(1L))
                .thenReturn(productData(1L, "IPHONE-15", "iPhone 15"));

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.code").value("IPHONE-15"));

        verify(productFacade).getProductById(1L);
    }

    @Test
    void getProductById_shouldReturn404_whenFacadeThrowsNotFound() throws Exception {
        when(productFacade.getProductById(999L))
                .thenThrow(new ProductNotFoundException("Product not found with id: 999"));

        mockMvc.perform(get("/api/products/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void getAllProducts_shouldReturn200() throws Exception {
        when(productFacade.getAllProducts())
                .thenReturn(List.of(productData(1L, "IPHONE-15", "iPhone 15")));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("IPHONE-15"));

        verify(productFacade).getAllProducts();
    }

    @Test
    void updateProduct_shouldReturn200() throws Exception {
        ProductRequest request = validRequest("IPHONE-15", "iPhone 15 Updated");
        ProductData response = productData(1L, "IPHONE-15", "iPhone 15 Updated");

        when(productFacade.updateProduct(eq(1L), any(ProductRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("iPhone 15 Updated"));

        verify(productFacade).updateProduct(eq(1L), any(ProductRequest.class));
    }

    @Test
    void deleteProduct_shouldReturn204() throws Exception {
        doNothing().when(productFacade).deleteProduct(1L);

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());

        verify(productFacade).deleteProduct(1L);
    }

    @Test
    void createProduct_shouldReturn409_whenDuplicateProductExceptionIsThrown() throws Exception {
        ProductRequest request = validRequest("IPHONE-15", "iPhone 15");

        when(productFacade.createProduct(any(ProductRequest.class)))
                .thenThrow(new DuplicateProductException(
                        "Product already exists with code: IPHONE-15"));

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    private ProductRequest validRequest(String code, String name) {
        return ProductRequest.builder()
                .code(code)
                .name(name)
                .description("Apple smartphone")
                .price(new BigDecimal("69999.00"))
                .stock(25)
                .active(true)
                .build();
    }

    private ProductData productData(Long id, String code, String name) {
        return ProductData.builder()
                .id(id)
                .code(code)
                .name(name)
                .description("Apple smartphone")
                .price(new BigDecimal("69999.00"))
                .stock(25)
                .active(true)
                .build();
    }
}
