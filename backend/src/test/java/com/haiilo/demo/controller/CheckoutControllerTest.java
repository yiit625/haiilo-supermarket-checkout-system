package com.haiilo.demo.controller;

import com.haiilo.demo.dto.CheckoutRequest;
import com.haiilo.demo.entity.Product;
import com.haiilo.demo.service.CheckoutService;
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

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CheckoutController.class)
class CheckoutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("unused") // IDE false positive
    @MockitoBean
    private CheckoutService checkoutService;

    @SuppressWarnings("unused") // IDE false positive
    @MockitoBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCalculateTotalSuccessfully() throws Exception {
        // GIVEN
        List<Long> productIds = List.of(1L, 1L, 2L);
        CheckoutRequest request = new CheckoutRequest(productIds);

        Product apple = Product.builder().id(1L).name("Apple").unitPrice(new BigDecimal("0.30")).build();
        Product banana = Product.builder().id(2L).name("Banana").unitPrice(new BigDecimal("0.50")).build();

        // Mocking ProductService calls
        when(productService.findById(1L)).thenReturn(apple);
        when(productService.findById(2L)).thenReturn(banana);

        // Mocking CheckoutService calculation
        when(checkoutService.calculateTotal(anyList())).thenReturn(new BigDecimal("1.10"));

        // WHEN & THEN
        mockMvc.perform(post("/api/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("1.10"));
    }
}
