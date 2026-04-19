package com.gestionganado.infrastructure.adapter.in.web;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VaccineRequest {
    @NotBlank(message = "Name cannot be blank")
    private String name;
    
    @Min(value = 1, message = "Frequency days must be greater than 0")
    private Integer frequencyDays;
}
