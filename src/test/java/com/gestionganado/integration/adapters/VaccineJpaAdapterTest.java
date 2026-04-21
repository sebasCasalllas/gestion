package com.gestionganado.integration.adapters;

import com.gestionganado.domain.model.Vaccine;
import com.gestionganado.infrastructure.adapter.out.persistence.animal.adapter.VaccineJpaAdapter;
import com.gestionganado.infrastructure.adapter.out.persistence.animal.entities.VaccineEntity;
import com.gestionganado.infrastructure.adapter.out.persistence.animal.mappers.VaccineEntityMapper;
import com.gestionganado.infrastructure.adapter.out.persistence.animal.repositories.SpringDataVaccineRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VaccineJpaAdapterTest {

    @Mock
    private SpringDataVaccineRepository springDataVaccineRepository;

    @Mock
    private VaccineEntityMapper mapper;

    @InjectMocks
    private VaccineJpaAdapter vaccineJpaAdapter;

    @Test
    @DisplayName("Should save vaccine and return domain model")
    void shouldSaveVaccine() {
        // Arrange
        Vaccine vaccine = Vaccine.builder().name("Aftosa").build();
        VaccineEntity entity = new VaccineEntity();

        when(mapper.toEntity(any(Vaccine.class))).thenReturn(entity);
        when(springDataVaccineRepository.save(any(VaccineEntity.class))).thenReturn(entity);
        when(mapper.toDomain(any(VaccineEntity.class))).thenReturn(vaccine);

        // Act
        Vaccine result = vaccineJpaAdapter.save(vaccine);

        // Assert
        assertNotNull(result);
        verify(springDataVaccineRepository).save(entity);
    }

    @Test
    @DisplayName("Should find vaccine by id")
    void shouldFindById() {
        // Arrange
        UUID id = UUID.randomUUID();
        VaccineEntity entity = new VaccineEntity();
        Vaccine vaccine = Vaccine.builder().id(id).build();

        when(springDataVaccineRepository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(vaccine);

        // Act
        Optional<Vaccine> result = vaccineJpaAdapter.findById(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
    }
}
