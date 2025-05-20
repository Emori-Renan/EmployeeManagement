package com.example.crudapp.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.crudapp.model.Workplace;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
class WorkplaceRepositoryTest {
    @Autowired
    private WorkplaceRepository workplaceRepository;

    private Workplace testWorkplace;

    @BeforeEach
    public void setUp() {
        testWorkplace = new Workplace();
        testWorkplace.setWorkplaceName("Office A");
        testWorkplace.setHourlyWage(30.0);
        testWorkplace.setOvertimeMultiplier(1.5);
        testWorkplace.setEmployeeId(1L); // Assuming you have an employee with ID 1
        workplaceRepository.save(testWorkplace);
    }

    @Test
    void testFindById() {
        Workplace foundWorkplace = workplaceRepository.findById(testWorkplace.getId()).orElse(null);
        assertNotNull(foundWorkplace, "Workplace should be found by ID.");
        assertEquals(testWorkplace.getWorkplaceName(), foundWorkplace.getWorkplaceName(), "Workplace name does not match.");
        assertEquals(testWorkplace.getHourlyWage(), foundWorkplace.getHourlyWage(), "Hourly wage does not match.");
        assertEquals(testWorkplace.getOvertimeMultiplier(), foundWorkplace.getOvertimeMultiplier(), "Overtime multiplier does not match.");
    }

    @Test
    void testFindByName() {
        // Assuming you've added a findByName method to your WorkplaceRepository
        Workplace foundWorkplace = workplaceRepository.findByWorkplaceName("Office A").orElse(null);
        assertNotNull(foundWorkplace, "Workplace should be found by name.");
        assertEquals(testWorkplace.getWorkplaceName(), foundWorkplace.getWorkplaceName(), "Workplace name does not match.");
        assertEquals(testWorkplace.getHourlyWage(), foundWorkplace.getHourlyWage(), "Hourly wage does not match.");
        assertEquals(testWorkplace.getOvertimeMultiplier(), foundWorkplace.getOvertimeMultiplier(), "Overtime multiplier does not match.");
    }
}