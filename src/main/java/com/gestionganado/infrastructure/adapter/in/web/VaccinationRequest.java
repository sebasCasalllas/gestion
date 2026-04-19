package com.gestionganado.infrastructure.adapter.in.web;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class VaccinationRequest {
    @NotNull(message = "Animal ID cannot be null")
    private UUID animalId;

    @NotNull(message = "Vaccine ID cannot be null")
    private UUID vaccineId;

    @NotNull(message = "Application date cannot be null")
    private LocalDate applicationDate;
}
