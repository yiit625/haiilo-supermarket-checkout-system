package com.haiilo.demo.integration;

import com.haiilo.demo.dto.BulkOfferRequest;
import com.haiilo.demo.dto.CheckoutRequest;
import com.haiilo.demo.dto.ProductRequest;
import com.haiilo.demo.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CheckoutIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("Should calculate total price correctly with bulk offers")
    void shouldCalculateTotalWithOffers() {
        String productUrl = "http://localhost:" + port + "/api/products";
        String offerUrl = "http://localhost:" + port + "/api/offers";
        String checkoutUrl = "http://localhost:" + port + "/api/checkout";

        // 1. Create products
        ProductResponse productA = restTemplate.postForObject(productUrl,
                new ProductRequest("Product A", new BigDecimal("50.00")), ProductResponse.class);
        ProductResponse productB = restTemplate.postForObject(productUrl,
                new ProductRequest("Product B", new BigDecimal("30.00")), ProductResponse.class);

        // 2. Define Offers
        BulkOfferRequest bulkOffer = new BulkOfferRequest(productA.id(), 3, new BigDecimal("130.00"), null);
        restTemplate.postForEntity(offerUrl, bulkOffer, Object.class);

        // 3. Checkout with multiple products
        List<Long> ids = Arrays.asList(productA.id(), productA.id(), productA.id(), productB.id());
        CheckoutRequest checkoutRequest = new CheckoutRequest(ids);

        // 4. Rest API
        ResponseEntity<String> response = restTemplate.postForEntity(
                checkoutUrl, checkoutRequest, String.class);

        // 5. Assertions
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        BigDecimal actualTotal = new BigDecimal(response.getBody());
        assertThat(actualTotal).isEqualByComparingTo(new BigDecimal("160.00"));
    }
}
