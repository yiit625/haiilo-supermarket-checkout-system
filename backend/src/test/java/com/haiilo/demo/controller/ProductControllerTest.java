package com.haiilo.demo.controller;

import com.haiilo.demo.dto.ProductRequest;
import com.haiilo.demo.entity.Product;
import com.haiilo.demo.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("unused") // IDE false positive
    @MockitoBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateProductSuccessfully() throws Exception {
        // GIVEN
        ProductRequest request = new ProductRequest("Apple", new BigDecimal("0.30"));
        Product savedProduct = Product.builder()
                .id(1L)
                .name("Apple")
                .unitPrice(new BigDecimal("0.30"))
                .build();

        when(productService.createProduct(any(Product.class))).thenReturn(savedProduct);

        // WHEN & THEN
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Apple"))
                .andExpect(jsonPath("$.unitPrice").value(0.30));
    }

    @Test
    void shouldReturnAllProductsSuccessfully() throws Exception {
        // GIVEN
        Product p1 = Product.builder().id(1L).name("Apple").unitPrice(new BigDecimal("0.30")).build();
        Product p2 = Product.builder().id(2L).name("Banana").unitPrice(new BigDecimal("0.50")).build();

        when(productService.findAll()).thenReturn(List.of(p1, p2));

        // WHEN & THEN
        mockMvc.perform(get("/api/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Apple"))
                .andExpect(jsonPath("$[1].name").value("Banana"))
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void shouldReturn400WhenPriceIsMissing() throws Exception {
        // GIVEN: No price request
        ProductRequest invalidRequest = new ProductRequest("Apple", null);

        // WHEN & THEN
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}