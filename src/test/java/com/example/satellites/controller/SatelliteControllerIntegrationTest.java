package com.example.satellites.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Start the full Spring context and autoconfigure MockMvc
@SpringBootTest
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
@Transactional         // rollback after each test for isolation
class SatelliteControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    record Params(Double lat, Double lon, Double alt) {}
    record SatelliteReq(String name, String orbit, String launchDate, Params parameters) {}

    @Test
    void fullCrudFlow() throws Exception {
        // 1) POST -> create
        SatelliteReq createReq = new SatelliteReq(
                "Integration-Hubble",
                "LEO",
                "1990-04-24T12:33:00",
                new Params(51.5, -0.1, 547.0)
        );

        String createJson = objectMapper.writeValueAsString(createReq);

        var createResult = mvc.perform(post("/api/satellites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", startsWith("/api/satellites/")))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Integration-Hubble"))
                .andExpect(jsonPath("$.orbit").value("LEO"))
                .andExpect(jsonPath("$.parameters.lat").value(51.5))
                .andReturn();

        // extract id from response
        String responseBody = createResult.getResponse().getContentAsString();
        var node = objectMapper.readTree(responseBody);
        long id = node.get("id").asLong();

        // 2) GET /api/satellites -> list contains the created item
        mvc.perform(get("/api/satellites"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())))
                .andExpect(jsonPath("$[0].id", is((int) id)));

        // 3) PUT -> update
        SatelliteReq updateReq = new SatelliteReq(
                "Integration-Hubble-Updated",
                "LEO",
                "1990-04-24T12:33:00",
                new Params(52.0, -0.2, 550.0)
        );
        String updateJson = objectMapper.writeValueAsString(updateReq);

        mvc.perform(put("/api/satellites/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Integration-Hubble-Updated"))
                .andExpect(jsonPath("$.parameters.alt").value(550.0));

        // 4) GET /{id}/position -> only lat/lon/alt
        mvc.perform(get("/api/satellites/{id}/position", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lat").value(52.0))
                .andExpect(jsonPath("$.lon").value(-0.2))
                .andExpect(jsonPath("$.alt").value(550.0));

        // 5) DELETE -> then GET returns 404
        mvc.perform(delete("/api/satellites/{id}", id))
                .andExpect(status().isNoContent());

        mvc.perform(get("/api/satellites/{id}", id))
                .andExpect(status().isNotFound());
    }
}