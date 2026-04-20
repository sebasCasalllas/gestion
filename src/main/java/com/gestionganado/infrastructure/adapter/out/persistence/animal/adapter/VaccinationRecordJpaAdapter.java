package com.gestionganado.infrastructure.adapter.out.persistence.animal.adapter;

import com.gestionganado.domain.model.VaccinationRecord;
import com.gestionganado.domain.port.out.VaccinationRecordRepositoryPort;
import com.gestionganado.infrastructure.adapter.out.persistence.animal.entities.VaccinationRecordEntity;
import com.gestionganado.infrastructure.adapter.out.persistence.animal.mappers.VaccinationRecordEntityMapper;
import com.gestionganado.infrastructure.adapter.out.persistence.animal.repositories.SpringDataVaccinationRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class VaccinationRecordJpaAdapter implements VaccinationRecordRepositoryPort {

    private final SpringDataVaccinationRecordRepository springDataRepository;
    private final VaccinationRecordEntityMapper mapper;

    @Override
    public VaccinationRecord save(VaccinationRecord record) {
        if (record.getId() == null) {
            record.setId(UUID.randomUUID());
        }
        VaccinationRecordEntity entity = mapper.toEntity(record);
        return mapper.toDomain(springDataRepository.save(entity));
    }

    @Override
    public boolean existsByAnimalIdAndVaccineIdAndApplicationDate(UUID animalId, UUID vaccineId, LocalDate applicationDate) {
        return springDataRepository.existsByAnimalIdAndVaccineIdAndApplicationDate(animalId, vaccineId, applicationDate);
    }

    @Override
    public List<VaccinationRecord> findUpcomingVaccinations(LocalDate fromDate, LocalDate toDate) {
        return springDataRepository.findUpcomingVaccinations(fromDate, toDate)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
