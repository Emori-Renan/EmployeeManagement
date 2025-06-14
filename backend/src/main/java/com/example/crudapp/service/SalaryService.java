package com.example.crudapp.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.crudapp.dto.SalaryDto;
import com.example.crudapp.dto.ServiceResponse;
import com.example.crudapp.model.Employee;
import com.example.crudapp.model.Salary;
import com.example.crudapp.repository.EmployeeRepository; 
import com.example.crudapp.repository.SalaryRepository;

@Service
public class SalaryService {

    private final SalaryRepository salaryRepository;
    private final EmployeeRepository employeeRepository;

    public SalaryService(SalaryRepository salaryRepository, EmployeeRepository employeeRepository) {
        this.salaryRepository = salaryRepository;
        this.employeeRepository = employeeRepository;
    }

    public ServiceResponse registerSalary(SalaryDto salaryDTO) {
        try {
            Optional<Salary> existingSalary = salaryRepository.findByEmployeeIdAndMonthYear(
                    salaryDTO.getEmployeeId(),
                    salaryDTO.getMonthYear());

            if (existingSalary.isPresent()) {
                return new ServiceResponse(false,
                        "Salary already exists for this employee for this month/year.");
            }

            Employee employee = employeeRepository.findById(salaryDTO.getEmployeeId())
                    .orElseThrow(() -> new IllegalArgumentException("Employee not found with ID: " + salaryDTO.getEmployeeId()));

            Salary salary = new Salary();
            salary.setEmployee(employee);
            salary.setMonthYear(salaryDTO.getMonthYear());
            salary.setTotalDaysWorked(salaryDTO.getTotalDaysWorked());
            salary.setTotalHours(salaryDTO.getTotalHours());
            salary.setOvertimeHours(salaryDTO.getOvertimeHours());
            salary.setTotalTransportCost(salaryDTO.getTotalTransportCost());
            salary.setFinalSalary(salaryDTO.getFinalSalary());

            Salary savedSalary = salaryRepository.save(salary);
            return new ServiceResponse(true, "Salary registered successfully", savedSalary);
        } catch (IllegalArgumentException e) {
            return new ServiceResponse(false, e.getMessage());
        } catch (Exception e) {
            return new ServiceResponse(false, "An error occurred while registering the salary: " + e.getMessage());
        }
    }
}