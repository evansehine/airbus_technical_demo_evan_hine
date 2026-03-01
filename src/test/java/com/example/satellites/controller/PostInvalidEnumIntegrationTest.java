package com.example.satellites.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Verifies that POSTing a payload with an invalid enum value for 'orbit' returns a parse error (400).
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PostInvalidEnumIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    record Params(Double lat, Double lon, Double alt) {}
    record SatelliteReq(String name, String orbit, String launchDate, Params parameters) {}

    @Test
    void postInvalidEnum_returnsBadRequestParseMessage() throws Exception {
        // 'orbit' = "LOW" is invalid (valid: LEO, MEO, GEO)
        var req = new SatelliteReq("InvalidSat", "LOW", "1990-04-24T12:33:00", new Params(1.0, 2.0, 3.0));
        String json = objectMapper.writeValueAsString(req);

        mvc.perform(post("/api/satellites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Bad Request")))
                .andExpect(jsonPath("$.message", containsString("Invalid value")));
    }
}