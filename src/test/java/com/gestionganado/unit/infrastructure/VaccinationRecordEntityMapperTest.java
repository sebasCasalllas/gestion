package com.gestionganado.unit.infrastructure;

import com.gestionganado.domain.model.Animal;
import com.gestionganado.domain.model.VaccinationRecord;
import com.gestionganado.domain.model.Vaccine;
import com.gestionganado.infrastructure.adapter.out.persistence.animal.entities.VaccinationRecordEntity;
import com.gestionganado.infrastructure.adapter.out.persistence.animal.mappers.AnimalEntityMapper;
import com.gestionganado.infrastructure.adapter.out.persistence.animal.mappers.VaccinationRecordEntityMapper;
import com.gestionganado.infrastructure.adapter.out.persistence.animal.mappers.VaccineEntityMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class VaccinationRecordEntityMapperTest {

    private final VaccinationRecordEntityMapper mapper = Mappers.getMapper(VaccinationRecordEntityMapper.class);
    private final AnimalEntityMapper animalMapper = Mappers.getMapper(AnimalEntityMapper.class);
    private final VaccineEntityMapper vaccineMapper = Mappers.getMapper(VaccineEntityMapper.class);

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(mapper, "animalEntityMapper", animalMapper);
        ReflectionTestUtils.setField(mapper, "vaccineEntityMapper", vaccineMapper);
    }

    @Test
    @DisplayName("Should map VaccinationRecord to VaccinationRecordEntity")
    void shouldMapToEntity() {
        // Arrange
        VaccinationRecord record = VaccinationRecord.builder()
                .id(UUID.randomUUID())
                .animal(Animal.builder().id(UUID.randomUUID()).build())
                .vaccine(Vaccine.builder().id(UUID.randomUUID()).build())
                .applicationDate(LocalDate.now())
                .nextDueDate(LocalDate.now().plusMonths(6))
                .build();

        // Act
        VaccinationRecordEntity entity = mapper.toEntity(record);

        // Assert
        assertNotNull(entity);
        assertEquals(record.getId(), entity.getId());
        assertNotNull(entity.getAnimal());
        assertEquals(record.getAnimal().getId(), entity.getAnimal().getId());
        assertNotNull(entity.getVaccine());
        assertEquals(record.getVaccine().getId(), entity.getVaccine().getId());
        assertEquals(record.getApplicationDate(), entity.getApplicationDate());
        assertEquals(record.getNextDueDate(), entity.getNextDueDate());
    }

    @Test
    @DisplayName("Should map VaccinationRecordEntity to VaccinationRecord")
    void shouldMapToDomain() {
        // Arrange
        VaccinationRecordEntity entity = new VaccinationRecordEntity();
        entity.setId(UUID.randomUUID());
        entity.setApplicationDate(LocalDate.now());
        entity.setNextDueDate(LocalDate.now().plusMonths(6));

        // Act
        VaccinationRecord record = mapper.toDomain(entity);

        // Assert
        assertNotNull(record);
        assertEquals(entity.getId(), record.getId());
        assertEquals(entity.getApplicationDate(), record.getApplicationDate());
        assertEquals(entity.getNextDueDate(), record.getNextDueDate());
    }
}
