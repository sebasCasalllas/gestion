package com.gestionganado.infrastructure.adapter.in.web;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class AnimalResponse {
    private UUID id;
    private String name;
    private String type;
    private LocalDate birthDate;
}
