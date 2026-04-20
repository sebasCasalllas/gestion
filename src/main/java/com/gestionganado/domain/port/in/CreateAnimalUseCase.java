package com.gestionganado.domain.port.in;

import com.gestionganado.domain.model.Animal;

public interface CreateAnimalUseCase {
    Animal createAnimal(Animal animal);
}
