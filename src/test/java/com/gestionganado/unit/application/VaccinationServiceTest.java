package com.gestionganado.unit.application;

import com.gestionganado.application.port.out.AnimalRepositoryPort;
import com.gestionganado.application.port.out.VaccinationRecordRepositoryPort;
import com.gestionganado.application.port.out.VaccineRepositoryPort;
import com.gestionganado.application.service.VaccinationService;
import com.gestionganado.domain.exception.DuplicateVaccinationException;
import com.gestionganado.domain.exception.ResourceNotFoundException;
import com.gestionganado.domain.model.Animal;
import com.gestionganado.domain.model.VaccinationRecord;
import com.gestionganado.domain.model.Vaccine;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VaccinationServiceTest {

    @Mock
    private VaccinationRecordRepositoryPort vaccinationRecordRepositoryPort;
    @Mock
    private AnimalRepositoryPort animalRepositoryPort;
    @Mock
    private VaccineRepositoryPort vaccineRepositoryPort;

    @InjectMocks
    private VaccinationService vaccinationService;

    @Test
    @DisplayName("Should apply vaccine successfully")
    void shouldApplyVaccineSuccessfully() {
        // Arrange
        UUID animalId = UUID.randomUUID();
        UUID vaccineId = UUID.randomUUID();
        Animal animal = Animal.builder().id(animalId).build();
        Vaccine vaccine = Vaccine.builder().id(vaccineId).frequencyDays(6).build();
        VaccinationRecord record = VaccinationRecord.builder()
                .animal(animal)
                .vaccine(vaccine)
                .applicationDate(LocalDate.now())
                .build();

        when(animalRepositoryPort.findById(animalId)).thenReturn(Optional.of(animal));
        when(vaccineRepositoryPort.findById(vaccineId)).thenReturn(Optional.of(vaccine));
        when(vaccinationRecordRepositoryPort.existsByAnimalIdAndVaccineIdAndApplicationDate(any(), any(), any())).thenReturn(false);
        when(vaccinationRecordRepositoryPort.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        VaccinationRecord result = vaccinationService.applyVaccine(record);

        // Assert
        assertNotNull(result);
        assertEquals(animal, result.getAnimal());
        assertEquals(vaccine, result.getVaccine());
        assertNotNull(result.getNextDueDate());
        verify(vaccinationRecordRepositoryPort, times(1)).save(record);
    }

    @Test
    @DisplayName("Should throw exception when animal not found")
    void shouldThrowExceptionWhenAnimalNotFound() {
        // Arrange
        UUID animalId = UUID.randomUUID();
        VaccinationRecord record = VaccinationRecord.builder()
                .animal(Animal.builder().id(animalId).build())
                .build();

        when(animalRepositoryPort.findById(animalId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> vaccinationService.applyVaccine(record));
    }

    @Test
    @DisplayName("Should throw exception when vaccination already exists")
    void shouldThrowExceptionWhenDuplicateVaccination() {
        // Arrange
        UUID animalId = UUID.randomUUID();
        UUID vaccineId = UUID.randomUUID();
        Animal animal = Animal.builder().id(animalId).build();
        Vaccine vaccine = Vaccine.builder().id(vaccineId).build();
        VaccinationRecord record = VaccinationRecord.builder()
                .animal(animal)
                .vaccine(vaccine)
                .applicationDate(LocalDate.now())
                .build();

        when(animalRepositoryPort.findById(animalId)).thenReturn(Optional.of(animal));
        when(vaccineRepositoryPort.findById(vaccineId)).thenReturn(Optional.of(vaccine));
        when(vaccinationRecordRepositoryPort.existsByAnimalIdAndVaccineIdAndApplicationDate(any(), any(), any())).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateVaccinationException.class, () -> vaccinationService.applyVaccine(record));
    }

    @ParameterizedTest
    @ValueSource(ints = {7, 30, 90})
    @DisplayName("Should get upcoming vaccinations for different day ranges")
    void shouldGetUpcomingVaccinations(int days) {
        // Arrange
        when(vaccinationRecordRepositoryPort.findUpcomingVaccinations(any(), any())).thenReturn(Collections.emptyList());

        // Act
        List<VaccinationRecord> result = vaccinationService.getUpcomingVaccinations(days);

        // Assert
        assertNotNull(result);
        verify(vaccinationRecordRepositoryPort).findUpcomingVaccinations(eq(LocalDate.now()), eq(LocalDate.now().plusDays(days)));
    }
}
