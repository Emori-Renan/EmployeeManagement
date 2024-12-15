package com.example.crudapp.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.crudapp.model.Employee;
import com.example.crudapp.model.Salary;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
class SalaryRepositoryTest {

    @Autowired
    private SalaryRepository salaryRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee testEmployee;

    @BeforeEach
    public void setUp() {
        testEmployee = new Employee();
        testEmployee.setEmployeeName("John Doe");
        testEmployee.setRole("employee");
        employeeRepository.save(testEmployee);
    }

     @Test
    void testFindByEmployeeIdAndMonthYear() {
        Salary salary = new Salary();
        salary.setEmployee(testEmployee);
        salary.setMonthYear("2024-11");
        salary.setTotalDaysWorked(20);
        salary.setFinalSalary(2000.0);
        salaryRepository.save(salary);

        Salary foundSalary = salaryRepository.findByEmployeeIdAndMonthYear(testEmployee.getId(), "2024-11").orElse(null);
        assertEquals(salary.getFinalSalary(), foundSalary.getFinalSalary(), "O salário não foi encontrado corretamente.");
    }
}
