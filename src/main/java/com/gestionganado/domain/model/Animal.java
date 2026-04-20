package com.gestionganado.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Animal {
    private UUID id;
    private String name;
    private String type;
    private LocalDate birthDate;
    private LocalDateTime createdAt;
}
