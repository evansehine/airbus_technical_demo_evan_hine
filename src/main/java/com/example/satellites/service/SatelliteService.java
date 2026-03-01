package com.example.satellites.service;

import com.example.satellites.entity.Satellite;
import com.example.satellites.exception.SatelliteNotFoundException;
import com.example.satellites.repository.SatelliteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SatelliteService {

    private final SatelliteRepository repository;

    public SatelliteService(SatelliteRepository repository) {
        this.repository = repository;
    }

    public List<Satellite> findAll() {
        return repository.findAll();
    }

    public Optional<Satellite> findById(Long id) {
        return repository.findById(id);
    }

    public Satellite getByIdOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new SatelliteNotFoundException(id));
    }

    public Satellite save(Satellite s) {
        return repository.save(s);
    }

    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new SatelliteNotFoundException(id);
        }
        repository.deleteById(id);
    }
}