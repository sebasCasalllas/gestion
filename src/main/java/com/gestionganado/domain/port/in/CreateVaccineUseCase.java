package com.gestionganado.domain.port.in;

import com.gestionganado.domain.model.Vaccine;

public interface CreateVaccineUseCase {
    Vaccine createVaccine(Vaccine vaccine);
}
