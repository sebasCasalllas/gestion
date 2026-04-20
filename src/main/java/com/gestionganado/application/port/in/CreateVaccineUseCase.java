package com.gestionganado.application.port.in;

import com.gestionganado.domain.model.Vaccine;

public interface CreateVaccineUseCase {
    Vaccine createVaccine(Vaccine vaccine);
}
