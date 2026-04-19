package com.gestionganado.infrastructure.adapter.out.persistence;

import com.gestionganado.domain.model.Vaccine;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VaccineEntityMapper {
    VaccineEntity toEntity(Vaccine vaccine);
    Vaccine toDomain(VaccineEntity entity);
}
