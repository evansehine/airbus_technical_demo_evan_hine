package com.example.satellites.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * JPA entity representing a Satellite.
 *
 * Maps to the "satellites" table and contains basic satellite
 * metadata along with a one-to-one relationship to its
 * positional parameters.
 */
@Entity
@Table(name = "satellites")
public class Satellite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrbitType orbit;

    @Column(nullable = false)
    private LocalDateTime launchDate;

    /**
     * Positional parameters associated with this satellite.
     * Cascade and orphan removal ensure parameters are managed
     * with the lifecycle of the Satellite entity.
     */
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "parameters_id")
    private SatelliteParameters parameters;

    public Satellite() {}

    public Satellite(String name, OrbitType orbit, LocalDateTime launchDate, SatelliteParameters parameters) {
        this.name = name;
        this.orbit = orbit;
        this.launchDate = launchDate;
        this.parameters = parameters;
    }

    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public OrbitType getOrbit() { return orbit; }
    public void setOrbit(OrbitType orbit) { this.orbit = orbit; }

    public LocalDateTime getLaunchDate() { return launchDate; }
    public void setLaunchDate(LocalDateTime launchDate) { this.launchDate = launchDate; }

    public SatelliteParameters getParameters() { return parameters; }
    public void setParameters(SatelliteParameters parameters) { this.parameters = parameters; }
}
