package com.example.satellites.mapper;

import com.example.satellites.dto.SatelliteRequest;
import com.example.satellites.dto.SatelliteResponse;
import com.example.satellites.entity.Satellite;
import com.example.satellites.entity.SatelliteParameters;

public final class SatelliteMapper {

    private SatelliteMapper() {}

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

    public static SatelliteResponse toResponse(Satellite s) {
        SatelliteResponse resp = new SatelliteResponse();
        resp.setId(s.getId());
        resp.setName(s.getName());
        resp.setOrbit(s.getOrbit());
        resp.setLaunchDate(s.getLaunchDate());

        SatelliteResponse.Parameters p = new SatelliteResponse.Parameters(
                s.getParameters() != null ? s.getParameters().getLat() : null,
                s.getParameters() != null ? s.getParameters().getLon() : null,
                s.getParameters() != null ? s.getParameters().getAlt() : null
        );
        resp.setParameters(p);
        return resp;
    }
}