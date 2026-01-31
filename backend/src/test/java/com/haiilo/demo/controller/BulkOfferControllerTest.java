package com.haiilo.demo.controller;

import com.haiilo.demo.dto.BulkOfferRequest;
import com.haiilo.demo.entity.BulkOffer;
import com.haiilo.demo.entity.Product;
import com.haiilo.demo.service.BulkOfferService;
import com.haiilo.demo.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BulkOfferController.class)
public class BulkOfferControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("unused") // IDE false positive
    @MockitoBean
    private BulkOfferService bulkOfferService;

    @SuppressWarnings("unused") // IDE false positive
    @MockitoBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateBulkOfferSuccessfully() throws Exception {
        // GIVEN
        BulkOfferRequest request = new BulkOfferRequest(1L, 5, new BigDecimal("1.20"));

        Product mockProduct = Product.builder().id(1L).name("Apple").build();
        when(productService.findById(1L)).thenReturn(mockProduct);

        BulkOffer savedBulkOffer = BulkOffer.builder()
                .id(1L)
                .product(mockProduct)
                .requiredQuantity(5)
                .offerPrice(new BigDecimal("1.20"))
                .build();

        when(bulkOfferService.createOffer(any(BulkOffer.class))).thenReturn(savedBulkOffer);

        // ASSERT
        mockMvc.perform(post("/api/offers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.requiredQuantity").value(5))
                .andExpect(jsonPath("$.offerPrice").value(1.20));
    }

    @Test
    void shouldDeleteBulkOfferSuccessfully() throws Exception {
        // GIVEN
        Long offerId = 1L;

        // WHEN & THEN
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/api/offers/{id}", offerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
