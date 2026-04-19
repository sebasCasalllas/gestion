package com.gestionganado.infrastructure.adapter.out.persistence;

import com.gestionganado.domain.model.Animal;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AnimalEntityMapper {
    AnimalEntity toEntity(Animal animal);
    Animal toDomain(AnimalEntity entity);
}
