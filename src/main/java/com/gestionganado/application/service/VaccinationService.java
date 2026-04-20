package com.gestionganado.application.service;

import com.gestionganado.domain.exception.DuplicateVaccinationException;
import com.gestionganado.domain.exception.ResourceNotFoundException;
import com.gestionganado.domain.model.Animal;
import com.gestionganado.domain.model.VaccinationRecord;
import com.gestionganado.domain.model.Vaccine;
import com.gestionganado.domain.port.in.ApplyVaccineUseCase;
import com.gestionganado.domain.port.in.GetUpcomingVaccinationsUseCase;
import com.gestionganado.domain.port.out.AnimalRepositoryPort;
import com.gestionganado.domain.port.out.VaccinationRecordRepositoryPort;
import com.gestionganado.domain.port.out.VaccineRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VaccinationService implements ApplyVaccineUseCase, GetUpcomingVaccinationsUseCase {

    private final VaccinationRecordRepositoryPort vaccinationRecordRepositoryPort;
    private final AnimalRepositoryPort animalRepositoryPort;
    private final VaccineRepositoryPort vaccineRepositoryPort;

    @Override
    public VaccinationRecord applyVaccine(VaccinationRecord vaccinationRecord) {
        Animal animal = animalRepositoryPort.findById(vaccinationRecord.getAnimal().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Animal", vaccinationRecord.getAnimal().getId().toString()));
        
        Vaccine vaccine = vaccineRepositoryPort.findById(vaccinationRecord.getVaccine().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Vaccine", vaccinationRecord.getVaccine().getId().toString()));

        boolean exists = vaccinationRecordRepositoryPort.existsByAnimalIdAndVaccineIdAndApplicationDate(
                animal.getId(), vaccine.getId(), vaccinationRecord.getApplicationDate());
        if (exists) {
            throw new DuplicateVaccinationException("Vaccination already recorded for this animal, vaccine and date");
        }

        vaccinationRecord.setAnimal(animal);
        vaccinationRecord.setVaccine(vaccine);
        vaccinationRecord.calculateNextDueDate();
        vaccinationRecord.setCreatedAt(LocalDateTime.now());

        return vaccinationRecordRepositoryPort.save(vaccinationRecord);
    }

    @Override
    public List<VaccinationRecord> getUpcomingVaccinations(int days) {
        LocalDate fromDate = LocalDate.now();
        LocalDate toDate = fromDate.plusDays(days);
        return vaccinationRecordRepositoryPort.findUpcomingVaccinations(fromDate, toDate);
    }
}
