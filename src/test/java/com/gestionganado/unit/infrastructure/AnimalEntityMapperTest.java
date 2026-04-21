package com.gestionganado.unit.infrastructure;

import com.gestionganado.domain.model.Animal;
import com.gestionganado.infrastructure.adapter.out.persistence.animal.entities.AnimalEntity;
import com.gestionganado.infrastructure.adapter.out.persistence.animal.mappers.AnimalEntityMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AnimalEntityMapperTest {

    private final AnimalEntityMapper mapper = Mappers.getMapper(AnimalEntityMapper.class);

    @Test
    @DisplayName("Should map Animal to AnimalEntity")
    void shouldMapToEntity() {
        // Arrange
        Animal animal = Animal.builder()
                .id(UUID.randomUUID())
                .name("Lola")
                .type("Vaca")
                .birthDate(LocalDate.now())
                .build();

        // Act
        AnimalEntity entity = mapper.toEntity(animal);

        // Assert
        assertNotNull(entity);
        assertEquals(animal.getId(), entity.getId());
        assertEquals(animal.getName(), entity.getName());
        assertEquals(animal.getType(), entity.getType());
        assertEquals(animal.getBirthDate(), entity.getBirthDate());
    }

    @Test
    @DisplayName("Should map AnimalEntity to Animal")
    void shouldMapToDomain() {
        // Arrange
        AnimalEntity entity = new AnimalEntity();
        entity.setId(UUID.randomUUID());
        entity.setName("Lola");
        entity.setType("Vaca");
        entity.setBirthDate(LocalDate.now());

        // Act
        Animal animal = mapper.toDomain(entity);

        // Assert
        assertNotNull(animal);
        assertEquals(entity.getId(), animal.getId());
        assertEquals(entity.getName(), animal.getName());
        assertEquals(entity.getType(), animal.getType());
        assertEquals(entity.getBirthDate(), animal.getBirthDate());
    }
}
