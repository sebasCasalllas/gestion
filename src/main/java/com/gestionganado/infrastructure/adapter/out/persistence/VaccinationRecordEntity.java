package com.gestionganado.infrastructure.adapter.out.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "vaccination_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VaccinationRecordEntity {
    @Id
    private UUID id;
    
    @ManyToOne
    @JoinColumn(name = "animal_id")
    private AnimalEntity animal;

    @ManyToOne
    @JoinColumn(name = "vaccine_id")
    private VaccineEntity vaccine;

    private LocalDate applicationDate;
    private LocalDate nextDueDate;
    private LocalDateTime createdAt;
}
