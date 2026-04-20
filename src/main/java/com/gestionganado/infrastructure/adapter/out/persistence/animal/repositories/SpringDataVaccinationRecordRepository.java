package com.gestionganado.infrastructure.adapter.out.persistence.animal.repositories;

import com.gestionganado.infrastructure.adapter.out.persistence.animal.entities.VaccinationRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface SpringDataVaccinationRecordRepository extends JpaRepository<VaccinationRecordEntity, UUID> {
    
    boolean existsByAnimalIdAndVaccineIdAndApplicationDate(UUID animalId, UUID vaccineId, LocalDate applicationDate);

    @Query("SELECT v FROM VaccinationRecordEntity v WHERE v.nextDueDate BETWEEN :fromDate AND :toDate")
    List<VaccinationRecordEntity> findUpcomingVaccinations(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);
}
