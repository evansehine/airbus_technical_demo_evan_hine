package com.example.satellites.service;

import com.example.satellites.entity.OrbitType;
import com.example.satellites.entity.Satellite;
import com.example.satellites.entity.SatelliteParameters;
import com.example.satellites.exception.SatelliteNotFoundException;
import com.example.satellites.repository.SatelliteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link com.example.satellites.service.SatelliteService}.
 *
 * Uses Mockito to mock repository interactions and verifies service behavior
 * for common CRUD scenarios and error paths.
 */
@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class SatelliteServiceTest {

    @Mock
    private SatelliteRepository repository;

    @InjectMocks
    private SatelliteService service;

    private Satellite sample;

    @BeforeEach
    void setUp() {
        // Build a sample Satellite (without setting ID)
        sample = new Satellite();
        sample.setName("TestSat");
        sample.setOrbit(OrbitType.LEO);
        sample.setLaunchDate(LocalDateTime.of(2020,1,1,0,0));
        sample.setParameters(new SatelliteParameters(10.0, 20.0, 100.0));
    }

    /** Verifies save delegates to the repository and returns its result. */
    @Test
    void save_delegatesToRepository_and_returnsSavedEntity() {
        when(repository.save(any(Satellite.class))).thenReturn(sample);

        Satellite saved = service.save(sample);

        assertNotNull(saved);
        assertSame(sample, saved); // repo mock returns the same instance
        verify(repository, times(1)).save(sample);
    }

    /** When the repository returns a value, getByIdOrThrow should return it. */
    @Test
    void getByIdOrThrow_returnsEntityWhenFound() {
        when(repository.findById(42L)).thenReturn(Optional.of(sample));

        Satellite result = service.getByIdOrThrow(42L);

        assertNotNull(result);
        assertEquals("TestSat", result.getName());
        verify(repository, times(1)).findById(42L);
    }

    /** When the repository is empty, getByIdOrThrow should throw SatelliteNotFoundException. */
    @Test
    void getByIdOrThrow_throwsWhenNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(SatelliteNotFoundException.class, () -> service.getByIdOrThrow(99L));
        verify(repository, times(1)).findById(99L);
    }

    /** If the entity exists, deleteById should call the repository delete. */
    @Test
    void deleteById_deletesWhenExists() {
        when(repository.existsById(42L)).thenReturn(true);

        service.deleteById(42L);

        verify(repository, times(1)).deleteById(42L);
    }

    /** If the entity does not exist, deleteById should throw SatelliteNotFoundException. */
    @Test
    void deleteById_throwsWhenNotExists() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThrows(SatelliteNotFoundException.class, () -> service.deleteById(99L));
        verify(repository, times(1)).existsById(99L);
        verify(repository, never()).deleteById(99L);
    }
}
