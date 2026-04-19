package com.gestionganado.infrastructure.adapter.out.persistence;

import com.gestionganado.domain.model.VaccinationRecord;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {AnimalEntityMapper.class, VaccineEntityMapper.class})
public interface VaccinationRecordEntityMapper {
    VaccinationRecordEntity toEntity(VaccinationRecord record);
    VaccinationRecord toDomain(VaccinationRecordEntity entity);
}
