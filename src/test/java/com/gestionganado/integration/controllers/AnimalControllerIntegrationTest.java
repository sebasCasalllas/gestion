package com.gestionganado.integration.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gestionganado.application.port.in.CreateAnimalUseCase;
import com.gestionganado.domain.exception.DomainException;
import com.gestionganado.domain.model.Animal;
import com.gestionganado.infrastructure.adapter.in.controller.rest.AnimalController;
import com.gestionganado.infrastructure.adapter.in.request.AnimalRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AnimalController.class)
class AnimalControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateAnimalUseCase createAnimalUseCase;

    @Test
    @DisplayName("POST /api/v1/animals should return 201 Created")
    void shouldCreateAnimal() throws Exception {
        // Arrange
        AnimalRequest request = new AnimalRequest();
        request.setName("Lola");
        request.setType("Vaca");
        request.setBirthDate(LocalDate.now().minusYears(1));

        Animal createdAnimal = Animal.builder()
                .id(UUID.randomUUID())
                .name("Lola")
                .type("Vaca")
                .birthDate(request.getBirthDate())
                .build();

        when(createAnimalUseCase.createAnimal(any(Animal.class))).thenReturn(createdAnimal);

        // Act & Assert
        mockMvc.perform(post("/api/v1/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Lola"));
    }

    @Test
    @DisplayName("POST /api/v1/animals with invalid data should return 400 Bad Request")
    void shouldReturn400WhenDataIsInvalid() throws Exception {
        // Arrange
        AnimalRequest request = new AnimalRequest();
        request.setName(""); // Invalid name

        // Act & Assert
        mockMvc.perform(post("/api/v1/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    //Revisar
    @Test
    @DisplayName("POST /api/v1/animals should return 400 when DomainException occurs")
    void shouldReturn400WhenDomainExceptionOccurs() throws Exception {
        // Arrange
        AnimalRequest request = new AnimalRequest();
        request.setName("Lola");
        request.setType("Vaca");
        request.setBirthDate(LocalDate.now().plusDays(1));

        when(createAnimalUseCase.createAnimal(any())).thenThrow(new DomainException("Invalid date"));

        // Act & Assert
        mockMvc.perform(post("/api/v1/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Validation Error"));
    }
}

