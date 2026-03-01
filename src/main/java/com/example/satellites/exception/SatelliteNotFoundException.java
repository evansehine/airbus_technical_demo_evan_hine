package com.example.satellites.exception;

public class SatelliteNotFoundException extends RuntimeException {
    public SatelliteNotFoundException(Long id) {
        super("Satellite with id " + id + " not found");
    }
}