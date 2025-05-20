package com.example.crudapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.crudapp.dto.ServiceResponse;
import com.example.crudapp.dto.WorkplaceRegistrationDTO;
import com.example.crudapp.model.Employee;
import com.example.crudapp.model.Workplace;
import com.example.crudapp.repository.EmployeeRepository;
import com.example.crudapp.repository.WorkplaceRepository;

@Service
public class WorkplaceService {

    private final WorkplaceRepository repository;
    private final EmployeeRepository employeeRepository;

    public WorkplaceService(WorkplaceRepository repository, EmployeeRepository employeeRepository) {
        this.repository = repository;
        this.employeeRepository = employeeRepository;
    }

    public ServiceResponse registerWorkplace(WorkplaceRegistrationDTO workplaceDTO) {

        if (workplaceDTO.getWorkplaceName() == null || workplaceDTO.getWorkplaceName().trim().isEmpty() ||
                workplaceDTO.getHourlyWage() <= 0 ||
                workplaceDTO.getOvertimeMultiplier() <= 0) {
            return ServiceResponse.error("Fill the fields correctly. Workplace Name cannot be empty, and Hourly Wage and Overtime Multiplier must be positive.");
        }
        try {
            Optional<Employee> employeeOptional = employeeRepository.findById(workplaceDTO.getEmployeeId());
            if (employeeOptional.isEmpty()) {
                return ServiceResponse.error("Employee not found"); // Handle the case where the employee doesn't exist
            }
            Employee employee = employeeOptional.get();
            // Map DTO to Entity
            Workplace workplace = new Workplace();
            workplace.setWorkplaceName(workplaceDTO.getWorkplaceName());
            workplace.setHourlyWage(workplaceDTO.getHourlyWage());
            workplace.setOvertimeMultiplier(workplaceDTO.getOvertimeMultiplier());
            employee.setId(workplaceDTO.getEmployeeId());
            workplace.setEmployee(employee);

            Workplace savedWorkplace = repository.save(workplace);

            return ServiceResponse.success("Workplace registered successfully", savedWorkplace);

        } catch (Exception e) {
            return ServiceResponse.error("An error occurred while registering the workplace: " + e.getMessage());
        }
    }

    public ServiceResponse getAllWorkplacesByEmployeeId(Long employeeId) {
        try {
            List<Workplace> workplaces = repository.findAllByEmployeeId(employeeId);
            if (workplaces.isEmpty()) {
                return ServiceResponse.error("No workplaces found for this employee.");
            }
            return ServiceResponse.success("Workplaces retrieved successfully.", workplaces);
        } catch (Exception e) {
            return ServiceResponse.error("Error while retrieving workplaces: " + e.getMessage());
        }
    }
}