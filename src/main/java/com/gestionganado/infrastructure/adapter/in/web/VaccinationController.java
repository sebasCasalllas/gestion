package com.gestionganado.infrastructure.adapter.in.web;

import com.gestionganado.domain.model.Animal;
import com.gestionganado.domain.model.VaccinationRecord;
import com.gestionganado.domain.model.Vaccine;
import com.gestionganado.domain.port.in.ApplyVaccineUseCase;
import com.gestionganado.domain.port.in.GetUpcomingVaccinationsUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/vaccinations")
@RequiredArgsConstructor
public class VaccinationController {

    private final ApplyVaccineUseCase applyVaccineUseCase;
    private final GetUpcomingVaccinationsUseCase getUpcomingVaccinationsUseCase;

    @PostMapping
    public ResponseEntity<VaccinationResponse> applyVaccine(@Valid @RequestBody VaccinationRequest request) {
        Animal animal = Animal.builder().id(request.getAnimalId()).build();
        Vaccine vaccine = Vaccine.builder().id(request.getVaccineId()).build();

        VaccinationRecord record = VaccinationRecord.builder()
                .animal(animal)
                .vaccine(vaccine)
                .applicationDate(request.getApplicationDate())
                .build();

        VaccinationRecord appliedRecord = applyVaccineUseCase.applyVaccine(record);

        VaccinationResponse response = VaccinationResponse.builder()
                .id(appliedRecord.getId())
                .animalName(appliedRecord.getAnimal().getName())
                .vaccineName(appliedRecord.getVaccine().getName())
                .applicationDate(appliedRecord.getApplicationDate())
                .nextDueDate(appliedRecord.getNextDueDate())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<UpcomingVaccinationResponse>> getUpcomingVaccinations(
            @RequestParam(name = "days", defaultValue = "30") int days) {
        
        List<VaccinationRecord> upcoming = getUpcomingVaccinationsUseCase.getUpcomingVaccinations(days);

        List<UpcomingVaccinationResponse> response = upcoming.stream()
                .map(record -> UpcomingVaccinationResponse.builder()
                        .id(record.getId())
                        .animalNombre(record.getAnimal().getName())
                        .vacuna(record.getVaccine().getName())
                        .nextDueDate(record.getNextDueDate())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}
