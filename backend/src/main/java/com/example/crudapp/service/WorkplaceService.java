package com.example.crudapp.service;

import org.springframework.stereotype.Service;

import com.example.crudapp.dto.ServiceResponse;
import com.example.crudapp.dto.WorkplaceRegistrationDTO;
import com.example.crudapp.model.Workplace;
import com.example.crudapp.repository.WorkplaceRepository;

@Service
public class WorkplaceService {

    private final WorkplaceRepository repository;

    public WorkplaceService(WorkplaceRepository repository) {
        this.repository = repository;
    }

    public ServiceResponse registerWorkplace(WorkplaceRegistrationDTO workplaceDTO) {
        if (workplaceDTO.getWorkplaceName().equals("") ||
                workplaceDTO.getDailySalary() <= 0) {
            return ServiceResponse.error("Fill the fields correctly.");
        }
        try {
            // Map DTO to Entity
            Workplace workplace = new Workplace();
            workplace.setWorkplaceName(workplaceDTO.getWorkplaceName());
            workplace.setDailySalary(workplaceDTO.getDailySalary());

            Workplace savedWorkplace = repository.save(workplace);

            return ServiceResponse.success("Employee registered successfully", savedWorkplace);

        } catch (Exception e) {
            return ServiceResponse.error("An error occurred while registering the employee: " + e.getMessage());
        }
    }
}
