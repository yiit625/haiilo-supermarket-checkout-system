package com.haiilo.demo.integration;

import com.haiilo.demo.dto.ProductRequest;
import com.haiilo.demo.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("Full Cycle: Create, Get and Delete Product")
    void fullProductLifecycleTest() {
        String baseUrl = "http://localhost:" + port + "/api/products";
        ProductRequest request = new ProductRequest("Banana", new BigDecimal("12.00"));

        // 1. CREATE (POST)
        ResponseEntity<ProductResponse> createResponse = restTemplate.postForEntity(
                baseUrl, request, ProductResponse.class);

        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Long createdId = createResponse.getBody().id();
        assertThat(createdId).isNotNull();

        // 2. GET BY ALL
        ResponseEntity<ProductResponse[]> getResponse = restTemplate.getForEntity(
                baseUrl, ProductResponse[].class);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(List.of(getResponse.getBody())).anyMatch(p -> p.id().equals(createdId));

        // 3. DELETE
        restTemplate.delete(baseUrl + "/" + createdId);

        // 4. VERIFY DELETION
        ResponseEntity<ProductResponse[]> finalGetResponse = restTemplate.getForEntity(
                baseUrl, ProductResponse[].class);

        assertThat(List.of(finalGetResponse.getBody())).noneMatch(p -> p.id().equals(createdId));
    }
}