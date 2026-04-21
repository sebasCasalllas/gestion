package com.gestionganado.integration.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gestionganado.application.port.in.CreateVaccineUseCase;
import com.gestionganado.domain.model.Vaccine;
import com.gestionganado.infrastructure.adapter.in.controller.rest.VaccineController;
import com.gestionganado.infrastructure.adapter.in.request.VaccineRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VaccineController.class)
class VaccineControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateVaccineUseCase createVaccineUseCase;

    @Test
    @DisplayName("POST /api/v1/vaccines should return 201 Created")
    void shouldCreateVaccine() throws Exception {
        // Arrange
        VaccineRequest request = new VaccineRequest();
        request.setName("Fiebre Aftosa");
        request.setFrequencyDays(180);

        Vaccine createdVaccine = Vaccine.builder()
                .id(UUID.randomUUID())
                .name("Fiebre Aftosa")
                .frequencyDays(180)
                .build();

        when(createVaccineUseCase.createVaccine(any(Vaccine.class))).thenReturn(createdVaccine);

        // Act & Assert
        mockMvc.perform(post("/api/v1/vaccines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Fiebre Aftosa"));
    }
}
