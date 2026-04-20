package com.gestionganado.infrastructure.adapter.out.persistence.animal.adapter;

import com.gestionganado.domain.model.Vaccine;
import com.gestionganado.domain.port.out.VaccineRepositoryPort;
import com.gestionganado.infrastructure.adapter.out.persistence.animal.entities.VaccineEntity;
import com.gestionganado.infrastructure.adapter.out.persistence.animal.mappers.VaccineEntityMapper;
import com.gestionganado.infrastructure.adapter.out.persistence.animal.repositories.SpringDataVaccineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class VaccineJpaAdapter implements VaccineRepositoryPort {

    private final SpringDataVaccineRepository springDataVaccineRepository;
    private final VaccineEntityMapper mapper;

    @Override
    public Vaccine save(Vaccine vaccine) {
        if (vaccine.getId() == null) {
            vaccine.setId(UUID.randomUUID());
        }
        VaccineEntity entity = mapper.toEntity(vaccine);
        return mapper.toDomain(springDataVaccineRepository.save(entity));
    }

    @Override
    public Optional<Vaccine> findById(UUID id) {
        return springDataVaccineRepository.findById(id).map(mapper::toDomain);
    }
}
