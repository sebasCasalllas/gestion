package com.gestionganado.domain.port.out;

import com.gestionganado.domain.model.VaccinationRecord;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface VaccinationRecordRepositoryPort {
    VaccinationRecord save(VaccinationRecord record);
    boolean existsByAnimalIdAndVaccineIdAndApplicationDate(UUID animalId, UUID vaccineId, LocalDate applicationDate);
    List<VaccinationRecord> findUpcomingVaccinations(LocalDate fromDate, LocalDate toDate);
}
