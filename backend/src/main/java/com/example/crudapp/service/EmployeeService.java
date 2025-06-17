package com.example.crudapp.service;

import org.springframework.stereotype.Service;

import com.example.crudapp.dto.EmployeeDTO;
import com.example.crudapp.dto.EmployeeRegistrationDTO;
import com.example.crudapp.dto.ServiceResponse;
import com.example.crudapp.model.Employee;
import com.example.crudapp.model.UserLogin;
import com.example.crudapp.repository.EmployeeRepository;
import com.example.crudapp.repository.UserLoginRepository;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final UserLoginRepository userLoginRepository;

    public EmployeeService(UserLoginRepository userLoginRepository, EmployeeRepository employeeRepository) {
        this.userLoginRepository = userLoginRepository;
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
        UserLogin userLogin = userLoginRepository.findByUsername(employeeDTO.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found with username: " + employeeDTO.getEmployeeName()));
            // Map DTO to Entity
            Employee employee = new Employee();
            employee.setEmployeeName(employeeDTO.getEmployeeName());
            employee.setRole(employeeDTO.getRole());
            employee.setUserLogin(userLogin);   
            Employee savedEmployee = employeeRepository.save(employee);
            EmployeeDTO responseSavedEmployee = new EmployeeDTO(
                    savedEmployee.getId(),
                    savedEmployee.getEmployeeName(),
                    savedEmployee.getRole()
            );
            return ServiceResponse.success("Employee registered successfully", responseSavedEmployee);
        } catch (Exception e) {
            return ServiceResponse.error("An error occurred while registering the employee: " + e.getMessage());
        }
    }

    public ServiceResponse getEmployees(String username) {
        try {
            UserLogin userLogin = userLoginRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
            return ServiceResponse.success("Employees retrieved successfully", employeeRepository.findByUserLogin(userLogin));
        } catch (Exception e) {
            return ServiceResponse.error("An error occurred while retrieving employees: " + e.getMessage());
        }
    }

    public ServiceResponse getEmployeeById(Long id) {
    try {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + id));

        EmployeeDTO dto = new EmployeeDTO(
                employee.getId(),
                employee.getEmployeeName(),
                employee.getRole()
        );

        return ServiceResponse.success("Employee retrieved successfully", dto);
    } catch (Exception e) {
        return ServiceResponse.error("An error occurred while retrieving the employee: " + e.getMessage());
    }
}


    public ServiceResponse updateEmployee(Long id, EmployeeRegistrationDTO employeeDTO) {
        try {
            Employee employee = employeeRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + id));
            if (employeeDTO.getEmployeeName() != null) {
                employee.setEmployeeName(employeeDTO.getEmployeeName());
            }
            if (employeeDTO.getRole() != null) {
                employee.setRole(employeeDTO.getRole());
            }
            Employee updatedEmployee = employeeRepository.save(employee);
            return ServiceResponse.success("Employee updated successfully", updatedEmployee);
        } catch (Exception e) {
            return ServiceResponse.error("An error occurred while updating the employee: " + e.getMessage());
        }
    }

}
