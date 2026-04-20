package com.gestionganado.infrastructure.adapter.out.persistence.animal.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "vaccines")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VaccineEntity {
    @Id
    private UUID id;
    private String name;
    private Integer frequencyDays;
    private LocalDateTime createdAt;
}
