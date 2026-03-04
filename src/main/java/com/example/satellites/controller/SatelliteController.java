package com.example.satellites.controller;

import com.example.satellites.dto.SatelliteRequest;
import com.example.satellites.dto.SatelliteResponse;
import com.example.satellites.entity.Satellite;
import com.example.satellites.entity.SatelliteParameters;
import com.example.satellites.mapper.SatelliteMapper;
import com.example.satellites.service.SatelliteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * REST controller providing CRUD operations for Satellite resources.
 */
@RestController
@RequestMapping("/api/satellites")
public class SatelliteController {

    private final SatelliteService service;

    public SatelliteController(SatelliteService service) {
        this.service = service;
    }

    /** Returns all satellites. */
    @GetMapping
    public ResponseEntity<List<SatelliteResponse>> getAll() {
        List<SatelliteResponse> list = service.findAll()
                .stream()
                .map(SatelliteMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    /** Returns a satellite by id or 404 if not found. */
    @GetMapping("/{id}")
    public ResponseEntity<SatelliteResponse> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(SatelliteMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** Creates a new satellite and returns 201 Created. */
    @PostMapping
    public ResponseEntity<SatelliteResponse> create(@Valid @RequestBody SatelliteRequest request) {
        Satellite entity = SatelliteMapper.toEntity(request);
        Satellite saved = service.save(entity);
        SatelliteResponse response = SatelliteMapper.toResponse(saved);
        URI location = URI.create("/api/satellites/" + saved.getId());
        return ResponseEntity.created(location).body(response);
    }

    /** Updates an existing satellite. */
    @PutMapping("/{id}")
    public ResponseEntity<SatelliteResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody SatelliteRequest request) {

        Satellite existing = service.getByIdOrThrow(id);

        existing.setName(request.getName());
        existing.setOrbit(request.getOrbit());
        existing.setLaunchDate(request.getLaunchDate());

        if (request.getParameters() != null) {
            SatelliteParameters params = existing.getParameters();
            if (params == null) {
                params = new SatelliteParameters();
            }
            params.setLat(request.getParameters().getLat());
            params.setLon(request.getParameters().getLon());
            params.setAlt(request.getParameters().getAlt());
            existing.setParameters(params);
        } else {
            existing.setParameters(null);
        }

        Satellite saved = service.save(existing);
        SatelliteResponse resp = SatelliteMapper.toResponse(saved);
        return ResponseEntity.ok(resp);
    }

    /** Deletes a satellite. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /** Returns only satellite position (lat, lon, alt). */
    @GetMapping("/{id}/position")
    public ResponseEntity<?> getPosition(@PathVariable Long id) {
        Satellite s = service.getByIdOrThrow(id);
        var p = s.getParameters();
        if (p == null) {
            return ResponseEntity.noContent().build();
        }
        Map<String, Object> body = Map.of("lat", p.getLat(), "lon", p.getLon(), "alt", p.getAlt());
        return ResponseEntity.ok(body);
    }
}
