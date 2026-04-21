package com.gestionganado.unit.domain;

import com.gestionganado.domain.model.VaccinationRecord;
import com.gestionganado.domain.model.Vaccine;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class VaccinationRecordTest {

    @Test
    @DisplayName("Should calculate next due date correctly")
    void shouldCalculateNextDueDate() {
        // Arrange
        Vaccine vaccine = Vaccine.builder().frequencyDays(180).build();
        LocalDate applicationDate = LocalDate.of(2024, 1, 1);
        VaccinationRecord record = VaccinationRecord.builder()
                .vaccine(vaccine)
                .applicationDate(applicationDate)
                .build();

        // Act
        record.calculateNextDueDate();

        // Assert
        assertEquals(LocalDate.of(2024, 6, 29), record.getNextDueDate()); // 2024 is leap year
    }

    @Test
    @DisplayName("Should not calculate next due date if data is missing")
    void shouldNotCalculateNextDueDateWhenDataMissing() {
        // Arrange
        VaccinationRecord record = VaccinationRecord.builder().build();

        // Act
        record.calculateNextDueDate();

        // Assert
        assertNull(record.getNextDueDate());
    }
}
