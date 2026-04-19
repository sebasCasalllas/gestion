package com.gestionganado.infrastructure.adapter.out.persistence;

import com.gestionganado.domain.model.Animal;
import com.gestionganado.domain.port.out.AnimalRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AnimalJpaAdapter implements AnimalRepositoryPort {

    private final SpringDataAnimalRepository springDataAnimalRepository;
    private final AnimalEntityMapper mapper;

    @Override
    public Animal save(Animal animal) {
        if (animal.getId() == null) {
            animal.setId(UUID.randomUUID());
        }
        AnimalEntity entity = mapper.toEntity(animal);
        return mapper.toDomain(springDataAnimalRepository.save(entity));
    }

    @Override
    public Optional<Animal> findById(UUID id) {
        return springDataAnimalRepository.findById(id).map(mapper::toDomain);
    }
}
