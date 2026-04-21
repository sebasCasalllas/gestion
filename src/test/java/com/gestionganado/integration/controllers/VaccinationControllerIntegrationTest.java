package com.gestionganado.integration.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gestionganado.application.port.in.ApplyVaccineUseCase;
import com.gestionganado.application.port.in.GetUpcomingVaccinationsUseCase;
import com.gestionganado.domain.exception.DuplicateVaccinationException;
import com.gestionganado.domain.exception.ResourceNotFoundException;
import com.gestionganado.domain.model.Animal;
import com.gestionganado.domain.model.VaccinationRecord;
import com.gestionganado.domain.model.Vaccine;
import com.gestionganado.infrastructure.adapter.in.controller.rest.VaccinationController;
import com.gestionganado.infrastructure.adapter.in.request.VaccinationRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VaccinationController.class)
class VaccinationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ApplyVaccineUseCase applyVaccineUseCase;

    @MockBean
    private GetUpcomingVaccinationsUseCase getUpcomingVaccinationsUseCase;

    @Test
    @DisplayName("POST /api/v1/vaccinations should return 201 Created")
    void shouldApplyVaccine() throws Exception {
        // Arrange
        VaccinationRequest request = new VaccinationRequest();
        request.setAnimalId(UUID.randomUUID());
        request.setVaccineId(UUID.randomUUID());
        request.setApplicationDate(LocalDate.now());

        VaccinationRecord appliedRecord = VaccinationRecord.builder()
                .id(UUID.randomUUID())
                .animal(Animal.builder().name("Lola").build())
                .vaccine(Vaccine.builder().name("Fiebre Aftosa").build())
                .applicationDate(request.getApplicationDate())
                .nextDueDate(request.getApplicationDate().plusMonths(6))
                .build();

        when(applyVaccineUseCase.applyVaccine(any())).thenReturn(appliedRecord);

        // Act & Assert
        mockMvc.perform(post("/api/v1/vaccinations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.animalName").value("Lola"));
    }

    @Test
    @DisplayName("GET /api/v1/vaccinations/upcoming should return 200 OK and map data correctly")
    void shouldGetUpcomingVaccinationsWithData() throws Exception {
        // Arrange
        VaccinationRecord record = VaccinationRecord.builder()
                .id(UUID.randomUUID())
                .animal(Animal.builder().name("Lola").build())
                .vaccine(Vaccine.builder().name("Fiebre Aftosa").build())
                .nextDueDate(LocalDate.now().plusDays(10))
                .build();

        when(getUpcomingVaccinationsUseCase.getUpcomingVaccinations(30)).thenReturn(List.of(record));

        // Act & Assert
        mockMvc.perform(get("/api/v1/vaccinations/upcoming")
                        .param("days", "30"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].animalNombre").value("Lola"))
                .andExpect(jsonPath("$[0].vacuna").value("Fiebre Aftosa"))
                .andExpect(jsonPath("$[0].nextDueDate").exists());
    }

    @Test
    @DisplayName("POST /api/v1/vaccinations should return 400 when duplicate vaccination")
    void shouldReturn400WhenDuplicateVaccination() throws Exception {
        // Arrange
        VaccinationRequest request = new VaccinationRequest();
        request.setAnimalId(UUID.randomUUID());
        request.setVaccineId(UUID.randomUUID());
        request.setApplicationDate(LocalDate.now());

        when(applyVaccineUseCase.applyVaccine(any())).thenThrow(new DuplicateVaccinationException("Duplicate"));

        // Act & Assert
        mockMvc.perform(post("/api/v1/vaccinations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Duplicate Record"));
    }

    @Test
    @DisplayName("POST /api/v1/vaccinations should return 404 when resource not found")
    void shouldReturn404WhenResourceNotFound() throws Exception {
        // Arrange
        VaccinationRequest request = new VaccinationRequest();
        request.setAnimalId(UUID.randomUUID());
        request.setVaccineId(UUID.randomUUID());
        request.setApplicationDate(LocalDate.now());

        when(applyVaccineUseCase.applyVaccine(any())).thenThrow(new ResourceNotFoundException("Animal", "123"));

        // Act & Assert
        mockMvc.perform(post("/api/v1/vaccinations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Not Found"));
    }
}


