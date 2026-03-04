package com.example.satellites.repository;

import com.example.satellites.entity.Satellite;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for Satellite entities.
 *
 * Extends JpaRepository to provide standard CRUD operations.
 */
public interface SatelliteRepository extends JpaRepository<Satellite, Long> {
}
