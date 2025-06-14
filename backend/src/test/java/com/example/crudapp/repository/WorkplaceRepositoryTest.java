package com.example.crudapp.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.crudapp.model.Employee;
import com.example.crudapp.model.Workplace;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
class WorkplaceRepositoryTest {
    @Autowired
    private WorkplaceRepository workplaceRepository;

    private Workplace testWorkplace;

    @InjectMocks
    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(1L); 
        employee.setEmployeeName("John Doe");
        employee.setRole("employee");

        testWorkplace = new Workplace();
        testWorkplace.setWorkplaceName("Office A");
        testWorkplace.setHourlyWage(30.0);
        testWorkplace.setOvertimeMultiplier(1.5);
        testWorkplace.setEmployee(employee); 
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
        Workplace foundWorkplace = workplaceRepository.findByWorkplaceName("Office A").orElse(null);
        assertNotNull(foundWorkplace, "Workplace should be found by name.");
        assertEquals(testWorkplace.getWorkplaceName(), foundWorkplace.getWorkplaceName(), "Workplace name does not match.");
        assertEquals(testWorkplace.getHourlyWage(), foundWorkplace.getHourlyWage(), "Hourly wage does not match.");
        assertEquals(testWorkplace.getOvertimeMultiplier(), foundWorkplace.getOvertimeMultiplier(), "Overtime multiplier does not match.");
    }
}