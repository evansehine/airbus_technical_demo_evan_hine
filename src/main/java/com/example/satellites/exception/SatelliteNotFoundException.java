package com.example.satellites.exception;

/**
 * Exception thrown when a Satellite resource cannot be found.
 */
public class SatelliteNotFoundException extends RuntimeException {

    public SatelliteNotFoundException(Long id) {
        super("Satellite with id " + id + " not found");
    }
}
