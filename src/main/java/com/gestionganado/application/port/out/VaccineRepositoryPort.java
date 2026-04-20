package com.gestionganado.application.port.out;

import com.gestionganado.domain.model.Vaccine;
import java.util.Optional;
import java.util.UUID;

public interface VaccineRepositoryPort {
    Vaccine save(Vaccine vaccine);
    Optional<Vaccine> findById(UUID id);
}
