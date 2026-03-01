package com.example.satellites.repository;

import com.example.satellites.entity.Satellite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SatelliteRepository extends JpaRepository<Satellite, Long> {
    // standard CRUD coverage provided by JpaRepository
}