package com.gestionganado.infrastructure.adapter.in.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AnimalRequest {
    @NotBlank(message = "Name cannot be blank")
    private String name;
    
    @NotBlank(message = "Type cannot be blank")
    private String type;
    
    @PastOrPresent(message = "Birth date must be in the past or present")
    private LocalDate birthDate;
}
