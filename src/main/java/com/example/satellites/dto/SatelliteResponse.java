package com.example.satellites.dto;

import com.example.satellites.entity.OrbitType;
import java.time.LocalDateTime;

/**
 * Response DTO representing a Satellite resource returned by the API.
 *
 * This DTO defines the external API contract and is decoupled from
 * the internal persistence entity.
 */
public class SatelliteResponse {

    private Long id;
    private String name;
    private OrbitType orbit;
    private LocalDateTime launchDate;
    private Parameters parameters;

    /**
     * Nested DTO containing positional parameters.
     */
    public static class Parameters {
        private Double lat;
        private Double lon;
        private Double alt;

        public Parameters() {}

        public Parameters(Double lat, Double lon, Double alt) {
            this.lat = lat;
            this.lon = lon;
            this.alt = alt;
        }

        public Double getLat() { return lat; }
        public void setLat(Double lat) { this.lat = lat; }
        public Double getLon() { return lon; }
        public void setLon(Double lon) { this.lon = lon; }
        public Double getAlt() { return alt; }
        public void setAlt(Double alt) { this.alt = alt; }
    }

    public SatelliteResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public OrbitType getOrbit() { return orbit; }
    public void setOrbit(OrbitType orbit) { this.orbit = orbit; }

    public LocalDateTime getLaunchDate() { return launchDate; }
    public void setLaunchDate(LocalDateTime launchDate) { this.launchDate = launchDate; }

    public Parameters getParameters() { return parameters; }
    public void setParameters(Parameters parameters) { this.parameters = parameters; }
}
