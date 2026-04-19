package com.gestionganado.infrastructure.adapter.in.web;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class VaccinationResponse {
    private UUID id;
    private String animalName;
    private String vaccineName;
    private LocalDate applicationDate;
    private LocalDate nextDueDate;
}
