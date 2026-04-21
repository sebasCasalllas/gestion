package com.gestionganado.infrastructure.adapter.in.controller.rest;

import com.gestionganado.domain.model.Animal;
import com.gestionganado.application.port.in.CreateAnimalUseCase;
import com.gestionganado.infrastructure.adapter.in.request.AnimalRequest;
import com.gestionganado.infrastructure.adapter.in.response.AnimalResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/animals")
@RequiredArgsConstructor
public class AnimalController {

    private final CreateAnimalUseCase createAnimalUseCase;

    @PostMapping
    public ResponseEntity<AnimalResponse> createAnimal(@Valid @RequestBody AnimalRequest request) {
        Animal animal = Animal.builder()
                .name(request.getName())
                .type(request.getType())
                .birthDate(request.getBirthDate())
                .build();

        Animal createdAnimal = createAnimalUseCase.createAnimal(animal);

        AnimalResponse response = AnimalResponse.builder()
                .id(createdAnimal.getId())
                .name(createdAnimal.getName())
                .type(createdAnimal.getType())
                .birthDate(createdAnimal.getBirthDate())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
