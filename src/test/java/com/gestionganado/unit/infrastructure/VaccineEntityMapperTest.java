package com.gestionganado.unit.infrastructure;

import com.gestionganado.domain.model.Vaccine;
import com.gestionganado.infrastructure.adapter.out.persistence.animal.entities.VaccineEntity;
import com.gestionganado.infrastructure.adapter.out.persistence.animal.mappers.VaccineEntityMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class VaccineEntityMapperTest {

    private final VaccineEntityMapper mapper = Mappers.getMapper(VaccineEntityMapper.class);

    @Test
    @DisplayName("Should map Vaccine to VaccineEntity")
    void shouldMapToEntity() {
        // Arrange
        Vaccine vaccine = Vaccine.builder()
                .id(UUID.randomUUID())
                .name("Aftosa")
                .frequencyDays(180)
                .build();

        // Act
        VaccineEntity entity = mapper.toEntity(vaccine);

        // Assert
        assertNotNull(entity);
        assertEquals(vaccine.getId(), entity.getId());
        assertEquals(vaccine.getName(), entity.getName());
        assertEquals(vaccine.getFrequencyDays(), entity.getFrequencyDays());
    }

    @Test
    @DisplayName("Should map VaccineEntity to Vaccine")
    void shouldMapToDomain() {
        // Arrange
        VaccineEntity entity = new VaccineEntity();
        entity.setId(UUID.randomUUID());
        entity.setName("Aftosa");
        entity.setFrequencyDays(180);

        // Act
        Vaccine vaccine = mapper.toDomain(entity);

        // Assert
        assertNotNull(vaccine);
        assertEquals(entity.getId(), vaccine.getId());
        assertEquals(entity.getName(), vaccine.getName());
        assertEquals(entity.getFrequencyDays(), vaccine.getFrequencyDays());
    }
}
