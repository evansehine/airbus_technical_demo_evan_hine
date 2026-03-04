package com.example.satellites.mapper;

import com.example.satellites.dto.SatelliteRequest;
import com.example.satellites.dto.SatelliteResponse;
import com.example.satellites.entity.Satellite;
import com.example.satellites.entity.SatelliteParameters;

/**
 * Simple utility class for mapping between DTOs and entities.
 */
public final class SatelliteMapper {

    private SatelliteMapper() {}

    /** Converts a request DTO into a Satellite entity. */
    public static Satellite toEntity(SatelliteRequest req) {
        SatelliteParameters params = new SatelliteParameters(
                req.getParameters().getLat(),
                req.getParameters().getLon(),
                req.getParameters().getAlt()
        );
        return new Satellite(
                req.getName(),
                req.getOrbit(),
                req.getLaunchDate(),
                params
        );
    }

    /** Converts a Satellite entity into a response DTO. */
    public static SatelliteResponse toResponse(Satellite s) {
        SatelliteResponse response = new SatelliteResponse();
        response.setId(s.getId());
        response.setName(s.getName());
        response.setOrbit(s.getOrbit());
        response.setLaunchDate(s.getLaunchDate());

        SatelliteResponse.Parameters p = new SatelliteResponse.Parameters(
                s.getParameters() != null ? s.getParameters().getLat() : null,
                s.getParameters() != null ? s.getParameters().getLon() : null,
                s.getParameters() != null ? s.getParameters().getAlt() : null
        );
        resp.setParameters(p);
        return response;
    }
}
