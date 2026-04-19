package com.gestionganado.infrastructure.adapter.in.web;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class VaccineResponse {
    private UUID id;
    private String name;
    private Integer frequencyDays;
}
