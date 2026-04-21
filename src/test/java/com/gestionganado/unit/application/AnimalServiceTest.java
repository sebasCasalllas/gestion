package com.gestionganado.unit.application;

import com.gestionganado.application.port.out.AnimalRepositoryPort;
import com.gestionganado.application.service.AnimalService;
import com.gestionganado.domain.exception.DomainException;
import com.gestionganado.domain.model.Animal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnimalServiceTest {

    @Mock
    private AnimalRepositoryPort animalRepositoryPort;

    @InjectMocks
    private AnimalService animalService;

    @Test
    @DisplayName("Should create animal successfully")
    void shouldCreateAnimalSuccessfully() {
        // Arrange
        Animal animal = Animal.builder()
                .name("Lola")
                .type("Vaca")
                .birthDate(LocalDate.now().minusYears(2))
                .build();

        Animal savedAnimal = Animal.builder()
                .id(UUID.randomUUID())
                .name("Lola")
                .type("Vaca")
                .birthDate(animal.getBirthDate())
                .createdAt(java.time.LocalDateTime.now())
                .build();

        when(animalRepositoryPort.save(any(Animal.class))).thenReturn(savedAnimal);

        // Act
        Animal result = animalService.createAnimal(animal);

        // Assert
        assertNotNull(result);
        assertEquals(savedAnimal.getId(), result.getId());
        verify(animalRepositoryPort, times(1)).save(animal);
    }

    @Test
    @DisplayName("Should throw exception when birth date is in the future")
    void shouldThrowExceptionWhenBirthDateIsInFuture() {
        // Arrange
        Animal animal = Animal.builder()
                .name("Lola")
                .type("Vaca")
                .birthDate(LocalDate.now().plusDays(1))
                .build();

        // Act & Assert
        assertThrows(DomainException.class, () -> animalService.createAnimal(animal));
        verify(animalRepositoryPort, never()).save(any());
    }
}
