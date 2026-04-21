package com.gestionganado.application.service;

import com.gestionganado.domain.exception.DomainException;
import com.gestionganado.domain.model.Vaccine;
import com.gestionganado.application.port.in.CreateVaccineUseCase;
import com.gestionganado.application.port.out.VaccineRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VaccineService implements CreateVaccineUseCase {

    private final VaccineRepositoryPort vaccineRepositoryPort;

    @Override
    public Vaccine createVaccine(Vaccine vaccine) {
        if (vaccine.getFrequencyDays() <= 0) {
            throw new DomainException("Frequency days must be greater than 0");
        }
        vaccine.setCreatedAt(LocalDateTime.now());
        return vaccineRepositoryPort.save(vaccine);
    }
}
