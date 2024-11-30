package com.example.crudapp.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        testWorkplace.setName("Office A");
        testWorkplace.setDailySalary(20000.0);
        workplaceRepository.save(testWorkplace);
    }

    @Test
    void testFindById() {
        Workplace foundWorkplace = workplaceRepository.findById(testWorkplace.getId()).orElse(null);
        assertEquals(testWorkplace.getName(), foundWorkplace.getName(), "O nome do local de trabalho não é igual.");
    }

    @Test
    void testFindByName() {
        Workplace foundWorkplace = workplaceRepository.findByName("Office A").orElse(null);
        assertEquals(testWorkplace.getName(), foundWorkplace.getName(), "Local de trabalho não encontrado pelo nome.");
    }
}
