package com.gestionganado.infrastructure.adapter.in.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class UpcomingVaccinationResponse {
    private UUID id;
    private String animalNombre;
    private String vacuna;
    private LocalDate nextDueDate;
}
