package com.example.crudapp.service;

import org.springframework.stereotype.Service;

import com.example.crudapp.dto.EmployeeRegistrationDTO;
import com.example.crudapp.dto.ServiceResponse;
import com.example.crudapp.model.Employee;
import com.example.crudapp.repository.EmployeeRepository;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public ServiceResponse registerEmployee(EmployeeRegistrationDTO employeeDTO) {
        if (employeeDTO.getRole().equals("") ||
                employeeDTO.getEmployeeName().equals("")) {
            return ServiceResponse.error("A name and a role are necessary.");
        }
        if (!employeeDTO.getRole().equalsIgnoreCase("admin") &&
                !employeeDTO.getRole().equalsIgnoreCase("employee")) {
            return ServiceResponse.error("Invalid role. Role must be 'admin' or 'employee'.");
        }

    try {
            // Map DTO to Entity
            Employee employee = new Employee();
            employee.setEmployeeName(employeeDTO.getEmployeeName());
            employee.setRole(employeeDTO.getRole().toUpperCase());

            Employee savedEmployee = employeeRepository.save(employee);

            return ServiceResponse.success("Employee registered successfully", savedEmployee);

        } catch (Exception e) {
            return ServiceResponse.error("An error occurred while registering the employee: " + e.getMessage());
        }
    }
}
