package com.gestionganado.domain.port.in;

import com.gestionganado.domain.model.VaccinationRecord;
import java.time.LocalDate;
import java.util.List;

public interface ApplyVaccineUseCase {
    VaccinationRecord applyVaccine(VaccinationRecord vaccinationRecord);
}
