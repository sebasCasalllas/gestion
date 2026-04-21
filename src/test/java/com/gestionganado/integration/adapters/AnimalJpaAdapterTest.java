package com.gestionganado.integration.adapters;

import com.gestionganado.domain.model.Animal;
import com.gestionganado.infrastructure.adapter.out.persistence.animal.adapter.AnimalJpaAdapter;
import com.gestionganado.infrastructure.adapter.out.persistence.animal.entities.AnimalEntity;
import com.gestionganado.infrastructure.adapter.out.persistence.animal.mappers.AnimalEntityMapper;
import com.gestionganado.infrastructure.adapter.out.persistence.animal.repositories.SpringDataAnimalRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnimalJpaAdapterTest {

    @Mock
    private SpringDataAnimalRepository springDataAnimalRepository;

    @Mock
    private AnimalEntityMapper mapper;

    @InjectMocks
    private AnimalJpaAdapter animalJpaAdapter;

    @Test
    @DisplayName("Should save animal and return domain model")
    void shouldSaveAnimal() {
        // Arrange
        Animal animal = Animal.builder().name("Lola").build();
        AnimalEntity entity = new AnimalEntity();
        entity.setName("Lola");

        when(mapper.toEntity(any(Animal.class))).thenReturn(entity);
        when(springDataAnimalRepository.save(any(AnimalEntity.class))).thenReturn(entity);
        when(mapper.toDomain(any(AnimalEntity.class))).thenReturn(animal);

        // Act
        Animal result = animalJpaAdapter.save(animal);

        // Assert
        assertNotNull(result);
        verify(springDataAnimalRepository).save(entity);
    }

    @Test
    @DisplayName("Should find animal by id")
    void shouldFindById() {
        // Arrange
        UUID id = UUID.randomUUID();
        AnimalEntity entity = new AnimalEntity();
        Animal animal = Animal.builder().id(id).build();

        when(springDataAnimalRepository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(animal);

        // Act
        Optional<Animal> result = animalJpaAdapter.findById(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
    }
}
