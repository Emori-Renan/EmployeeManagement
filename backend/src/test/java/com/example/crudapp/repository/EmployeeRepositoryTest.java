package com.example.crudapp.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.crudapp.model.Employee;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
class EmployeeRepositoryTest {
 @Autowired
    private EmployeeRepository employeeRepository;

    private Employee testEmployee;

    @BeforeEach
    public void setUp() {
        testEmployee = new Employee();
        testEmployee.setName("John Doe");
        testEmployee.setRole("employee");
        employeeRepository.save(testEmployee);
    }

    @Test
    void testFindById() {
        Employee foundEmployee = employeeRepository.findById(testEmployee.getId()).orElse(null);
        assertEquals(testEmployee.getName(), foundEmployee.getName(), "O nome do funcionário não é igual.");
    }

    @Test
    void testFindByName() {
        Employee foundEmployee = employeeRepository.findByName("John Doe").orElse(null);
        assertEquals(testEmployee.getName(), foundEmployee.getName(), "Funcionário não encontrado pelo nome.");
    }
}
