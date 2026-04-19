package com.gestionganado.domain.port.in;

import com.gestionganado.domain.model.VaccinationRecord;
import java.util.List;

public interface GetUpcomingVaccinationsUseCase {
    List<VaccinationRecord> getUpcomingVaccinations(int days);
}
