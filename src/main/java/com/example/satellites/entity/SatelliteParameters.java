package com.example.satellites.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "satellite_parameters")
public class SatelliteParameters {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // store as primitive double; validation will happen at DTO level
    private double lat;
    private double lon;
    private double alt;

    public SatelliteParameters() {}

    public SatelliteParameters(double lat, double lon, double alt) {
        this.lat = lat;
        this.lon = lon;
        this.alt = alt;
    }

    public Long getId() { return id; }
    public double getLat() { return lat; }
    public void setLat(double lat) { this.lat = lat; }
    public double getLon() { return lon; }
    public void setLon(double lon) { this.lon = lon; }
    public double getAlt() { return alt; }
    public void setAlt(double alt) { this.alt = alt; }
}