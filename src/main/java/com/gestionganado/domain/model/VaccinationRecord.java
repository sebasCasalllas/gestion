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
public class VaccinationRecord {
    private UUID id;
    private Animal animal;
    private Vaccine vaccine;
    private LocalDate applicationDate;
    private LocalDate nextDueDate;
    private LocalDateTime createdAt;

    public void calculateNextDueDate() {
        if (applicationDate != null && vaccine != null && vaccine.getFrequencyDays() != null) {
            this.nextDueDate = applicationDate.plusDays(vaccine.getFrequencyDays());
        }
    }
}
