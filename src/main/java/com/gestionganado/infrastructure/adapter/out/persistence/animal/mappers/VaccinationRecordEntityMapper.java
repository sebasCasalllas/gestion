package com.gestionganado.infrastructure.adapter.out.persistence.animal.mappers;

import com.gestionganado.domain.model.VaccinationRecord;
import com.gestionganado.infrastructure.adapter.out.persistence.animal.entities.VaccinationRecordEntity;
import com.gestionganado.infrastructure.adapter.out.persistence.animal.mappers.AnimalEntityMapper;
import com.gestionganado.infrastructure.adapter.out.persistence.animal.mappers.VaccineEntityMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {AnimalEntityMapper.class, VaccineEntityMapper.class})
public interface VaccinationRecordEntityMapper {
    VaccinationRecordEntity toEntity(VaccinationRecord record);
    VaccinationRecord toDomain(VaccinationRecordEntity entity);
}
