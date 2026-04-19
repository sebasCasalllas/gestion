package com.gestionganado.infrastructure.adapter.in.web;

import com.gestionganado.domain.model.Vaccine;
import com.gestionganado.domain.port.in.CreateVaccineUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/vaccines")
@RequiredArgsConstructor
public class VaccineController {

    private final CreateVaccineUseCase createVaccineUseCase;

    @PostMapping
    public ResponseEntity<VaccineResponse> createVaccine(@Valid @RequestBody VaccineRequest request) {
        Vaccine vaccine = Vaccine.builder()
                .name(request.getName())
                .frequencyDays(request.getFrequencyDays())
                .build();

        Vaccine createdVaccine = createVaccineUseCase.createVaccine(vaccine);

        VaccineResponse response = VaccineResponse.builder()
                .id(createdVaccine.getId())
                .name(createdVaccine.getName())
                .frequencyDays(createdVaccine.getFrequencyDays())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
