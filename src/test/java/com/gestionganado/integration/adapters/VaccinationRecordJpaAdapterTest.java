package com.gestionganado.integration.adapters;

import com.gestionganado.domain.model.VaccinationRecord;
import com.gestionganado.infrastructure.adapter.out.persistence.animal.adapter.VaccinationRecordJpaAdapter;
import com.gestionganado.infrastructure.adapter.out.persistence.animal.entities.VaccinationRecordEntity;
import com.gestionganado.infrastructure.adapter.out.persistence.animal.mappers.VaccinationRecordEntityMapper;
import com.gestionganado.infrastructure.adapter.out.persistence.animal.repositories.SpringDataVaccinationRecordRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VaccinationRecordJpaAdapterTest {

    @Mock
    private SpringDataVaccinationRecordRepository springDataRepository;

    @Mock
    private VaccinationRecordEntityMapper mapper;

    @InjectMocks
    private VaccinationRecordJpaAdapter vaccinationRecordJpaAdapter;

    @Test
    @DisplayName("Should save vaccination record")
    void shouldSaveRecord() {
        // Arrange
        VaccinationRecord record = VaccinationRecord.builder().build();
        VaccinationRecordEntity entity = new VaccinationRecordEntity();

        when(mapper.toEntity(any())).thenReturn(entity);
        when(springDataRepository.save(any())).thenReturn(entity);
        when(mapper.toDomain(any())).thenReturn(record);

        // Act
        VaccinationRecord result = vaccinationRecordJpaAdapter.save(record);

        // Assert
        assertNotNull(result);
        verify(springDataRepository).save(entity);
    }

    @Test
    @DisplayName("Should check if record exists")
    void shouldCheckIfExists() {
        // Arrange
        UUID animalId = UUID.randomUUID();
        UUID vaccineId = UUID.randomUUID();
        LocalDate date = LocalDate.now();
        when(springDataRepository.existsByAnimalIdAndVaccineIdAndApplicationDate(animalId, vaccineId, date)).thenReturn(true);

        // Act
        boolean result = vaccinationRecordJpaAdapter.existsByAnimalIdAndVaccineIdAndApplicationDate(animalId, vaccineId, date);

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("Should find upcoming vaccinations")
    void shouldFindUpcoming() {
        // Arrange
        LocalDate from = LocalDate.now();
        LocalDate to = from.plusDays(30);
        VaccinationRecordEntity entity = new VaccinationRecordEntity();
        when(springDataRepository.findUpcomingVaccinations(from, to)).thenReturn(List.of(entity));
        when(mapper.toDomain(entity)).thenReturn(VaccinationRecord.builder().build());

        // Act
        List<VaccinationRecord> result = vaccinationRecordJpaAdapter.findUpcomingVaccinations(from, to);

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }
}
