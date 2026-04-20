package com.gestionganado.infrastructure.adapter.out.persistence.animal.repositories;

import com.gestionganado.infrastructure.adapter.out.persistence.animal.entities.VaccineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SpringDataVaccineRepository extends JpaRepository<VaccineEntity, UUID> {
}
