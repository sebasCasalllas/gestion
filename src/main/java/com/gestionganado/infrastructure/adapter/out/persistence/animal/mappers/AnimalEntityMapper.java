package com.gestionganado.infrastructure.adapter.out.persistence.animal.mappers;

import com.gestionganado.domain.model.Animal;
import com.gestionganado.infrastructure.adapter.out.persistence.animal.entities.AnimalEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AnimalEntityMapper {
    AnimalEntity toEntity(Animal animal);
    Animal toDomain(AnimalEntity entity);
}
