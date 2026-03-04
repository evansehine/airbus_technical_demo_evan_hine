package com.example.satellites.dto;

import com.example.satellites.entity.OrbitType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;

import java.time.LocalDateTime;

/**
 * Request DTO for creating/updating a Satellite.
 *
 * Holds the fields accepted by the API. Validation annotations ensure
 * required fields and numeric ranges are enforced at the controller boundary.
 * {@code launchDate} is expected in the pattern {@code yyyy-MM-dd'T'HH:mm:ss}.
 */
public class SatelliteRequest {

    @NotBlank
    private String name;

    @NotNull
    private OrbitType orbit;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime launchDate;

    @Valid
    @NotNull
    private Parameters parameters;

    /**
     * Nested parameters object containing positional values.
     *
     * <p>Latitude/longitude are constrained to valid geographic ranges.
     * Altitude must be non-negative.</p>
     */
    public static class Parameters {
        @DecimalMin(value = "-90.0")
        @DecimalMax(value = "90.0")
        private Double lat;

        @DecimalMin(value = "-180.0")
        @DecimalMax(value = "180.0")
        private Double lon;

        @DecimalMin(value = "0.0")
        private Double alt;

        public Double getLat() { return lat; }
        public void setLat(Double lat) { this.lat = lat; }
        public Double getLon() { return lon; }
        public void setLon(Double lon) { this.lon = lon; }
        public Double getAlt() { return alt; }
        public void setAlt(Double alt) { this.alt = alt; }
    }

    // getters & setters
    public String getName(){return name;}
    public void setName(String name){this.name = name;}
    public OrbitType getOrbit(){return orbit;}
    public void setOrbit(OrbitType orbit){this.orbit = orbit;}
    public LocalDateTime getLaunchDate(){return launchDate;}
    public void setLaunchDate(LocalDateTime launchDate){this.launchDate = launchDate;}
    public Parameters getParameters(){return parameters;}
    public void setParameters(Parameters parameters){this.parameters = parameters;}
}
