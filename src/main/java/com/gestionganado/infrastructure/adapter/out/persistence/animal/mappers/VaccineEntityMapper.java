package com.gestionganado.infrastructure.adapter.out.persistence.animal.mappers;

import com.gestionganado.domain.model.Vaccine;
import com.gestionganado.infrastructure.adapter.out.persistence.animal.entities.VaccineEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VaccineEntityMapper {
    VaccineEntity toEntity(Vaccine vaccine);
    Vaccine toDomain(VaccineEntity entity);
}
