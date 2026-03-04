package com.example.satellites.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration test verifying that missing required fields
 * trigger validation errors.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PostMissingFieldIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Helper record used to build test payload
    record SatelliteReq(String orbit, String launchDate, Map<String, Object> parameters) {}

    /**
     * Ensures that omitting the 'name' field results in
     * a 400 Bad Request with validation error details.
     */
    @Test
    void postMissingName_returnsValidationErrors() throws Exception {
        var req = new SatelliteReq(
                "LEO",
                "1990-04-24T12:33:00",
                Map.of("lat", 1.0, "lon", 2.0, "alt", 3.0)
        );

        String json = objectMapper.writeValueAsString(req);

        mvc.perform(post("/api/satellites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Validation Failed")))
                .andExpect(jsonPath("$.errors", not(empty())))
                .andExpect(jsonPath("$.errors[*].field", hasItem("name")));
    }
}
