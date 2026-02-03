package com.haiilo.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haiilo.demo.dto.CheckoutRequest;
import com.haiilo.demo.dto.CheckoutResponse;
import com.haiilo.demo.dto.ItemDetail;
import com.haiilo.demo.service.CheckoutService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CheckoutController.class)
class CheckoutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    @SuppressWarnings("unused") // IDE false true
    private CheckoutService checkoutService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCalculateTotalSuccessfully() throws Exception {
        // GIVEN
        List<Long> productIds = List.of(1L, 2L);
        CheckoutRequest request = new CheckoutRequest(productIds);

        ItemDetail detail = new ItemDetail("Apple", 1, new BigDecimal("0.50"), new BigDecimal("0.50"), false);
        CheckoutResponse mockResponse = new CheckoutResponse(List.of(detail), new BigDecimal("0.50"));

        when(checkoutService.calculateTotalFromIds(productIds)).thenReturn(mockResponse);

        // WHEN & THEN
        mockMvc.perform(post("/api/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.finalTotal").value(0.50))
                .andExpect(jsonPath("$.items[0].productName").value("Apple"))
                .andExpect(jsonPath("$.items[0].discountApplied").value(false));
    }
}
