package com.gestionganado.application.port.out;

import com.gestionganado.domain.model.Animal;
import java.util.Optional;
import java.util.UUID;

public interface AnimalRepositoryPort {
    Animal save(Animal animal);
    Optional<Animal> findById(UUID id);
}
