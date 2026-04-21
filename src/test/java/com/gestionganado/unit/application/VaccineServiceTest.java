package com.gestionganado.unit.application;

import com.gestionganado.application.port.out.VaccineRepositoryPort;
import com.gestionganado.application.service.VaccineService;
import com.gestionganado.domain.exception.DomainException;
import com.gestionganado.domain.model.Vaccine;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VaccineServiceTest {

    @Mock
    private VaccineRepositoryPort vaccineRepositoryPort;

    @InjectMocks
    private VaccineService vaccineService;

    @Test
    @DisplayName("Should create vaccine successfully")
    void shouldCreateVaccineSuccessfully() {
        // Arrange
        Vaccine vaccine = Vaccine.builder()
                .name("Fiebre Aftosa")
                .frequencyDays(180)
                .build();

        Vaccine savedVaccine = Vaccine.builder()
                .id(UUID.randomUUID())
                .name("Fiebre Aftosa")
                .frequencyDays(180)
                .createdAt(java.time.LocalDateTime.now())
                .build();

        when(vaccineRepositoryPort.save(any(Vaccine.class))).thenReturn(savedVaccine);

        // Act
        Vaccine result = vaccineService.createVaccine(vaccine);

        // Assert
        assertNotNull(result);
        assertEquals(savedVaccine.getId(), result.getId());
        verify(vaccineRepositoryPort, times(1)).save(vaccine);
    }

    @Test
    @DisplayName("Should throw exception when frequency days is zero or negative")
    void shouldThrowExceptionWhenFrequencyDaysIsInvalid() {
        // Arrange
        Vaccine vaccine = Vaccine.builder()
                .name("Test")
                .frequencyDays(0)
                .build();

        // Act & Assert
        assertThrows(DomainException.class, () -> vaccineService.createVaccine(vaccine));
        verify(vaccineRepositoryPort, never()).save(any());
    }
}
