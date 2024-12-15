package com.example.crudapp.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import com.example.crudapp.model.Employee;
import com.example.crudapp.model.Workday;
import com.example.crudapp.model.Workplace;

import jakarta.transaction.Transactional;

@SpringBootTest
@DirtiesContext
@Transactional
class WorkdayRepositoryTest {
    @Autowired
    private WorkdayRepository workdayRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private WorkplaceRepository workplaceRepository;

    private Employee testEmployee;
    private Workplace testWorkplace;

     @BeforeEach
    public void setUp() {
        testEmployee = new Employee();
        testEmployee.setEmployeeName("John Doe");
        testEmployee.setRole("employee");
        employeeRepository.save(testEmployee);

        testWorkplace = new Workplace();
        testWorkplace.setWorkplaceName("Office A");
        workplaceRepository.save(testWorkplace);
    }

    @Test
    void testFindByEmployeeIdAndDateBetween() {
        Workday workday1 = new Workday();
        workday1.setEmployee(testEmployee);
        workday1.setWorkplace(testWorkplace);
        workday1.setDate(LocalDate.of(2024, 11, 1));
        workday1.setHoursWorked(8);
        workday1.setOvertimeHours(2);
        workday1.setTransportCost(50.0);
        workdayRepository.save(workday1);

        Workday workday2 = new Workday();
        workday2.setEmployee(testEmployee);
        workday2.setWorkplace(testWorkplace);
        workday2.setDate(LocalDate.of(2024, 11, 2));
        workday2.setHoursWorked(8);
        workday2.setOvertimeHours(1);
        workday2.setTransportCost(50.0);
        workdayRepository.save(workday2);

        List<Workday> workdays = workdayRepository.findByEmployeeIdAndDateBetween(testEmployee.getId(), 
                LocalDate.of(2024, 11, 1), LocalDate.of(2024, 11, 2));

        assertEquals(2, workdays.size(), "O número de dias trabalhados está incorreto.");
    }
}
