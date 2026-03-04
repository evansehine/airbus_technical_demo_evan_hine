package com.example.satellites.service;

import com.example.satellites.entity.Satellite;
import com.example.satellites.exception.SatelliteNotFoundException;
import com.example.satellites.repository.SatelliteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service layer responsible for Satellite business operations.
 *
 * Provides transactional CRUD operations and encapsulates
 * repository access and domain-specific error handling.
 */
@Service
@Transactional
public class SatelliteService {

    private final SatelliteRepository repository;

    public SatelliteService(SatelliteRepository repository) {
        this.repository = repository;
    }

    /** Returns all satellites. */
    public List<Satellite> findAll() {
        return repository.findAll();
    }

    /** Returns a satellite wrapped in Optional. */
    public Optional<Satellite> findById(Long id) {
        return repository.findById(id);
    }

    /**
     * Returns a satellite or throws SatelliteNotFoundException
     * if no satellite exists with the given id.
     */
    public Satellite getByIdOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new SatelliteNotFoundException(id));
    }

    /** Persists a satellite entity. */
    public Satellite save(Satellite s) {
        return repository.save(s);
    }

    /**
     * Deletes a satellite by id.
     * Throws SatelliteNotFoundException if it does not exist.
     */
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new SatelliteNotFoundException(id);
        }
        repository.deleteById(id);
    }
}
