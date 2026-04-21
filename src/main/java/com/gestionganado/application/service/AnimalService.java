package com.gestionganado.application.service;

import com.gestionganado.domain.exception.DomainException;
import com.gestionganado.domain.model.Animal;
import com.gestionganado.application.port.in.CreateAnimalUseCase;
import com.gestionganado.application.port.out.AnimalRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AnimalService implements CreateAnimalUseCase {

    private final AnimalRepositoryPort animalRepositoryPort;

    @Override
    public Animal createAnimal(Animal animal) {
        if (animal.getBirthDate().isAfter(LocalDate.now())) {
            throw new DomainException("Birth date cannot be in the future");
        }
        animal.setCreatedAt(LocalDateTime.now());
        return animalRepositoryPort.save(animal);
    }
}
