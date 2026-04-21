package com.gestionganado.application.port.in;

import com.gestionganado.domain.model.Animal;

public interface CreateAnimalUseCase {
    Animal createAnimal(Animal animal);
}
