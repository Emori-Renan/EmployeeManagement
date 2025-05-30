package com.example.crudapp.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.crudapp.dto.ServiceResponse;
import com.example.crudapp.dto.WorkdayDTO;
import com.example.crudapp.exporter.WorkdayExcelExporter;
import com.example.crudapp.model.Employee;
import com.example.crudapp.model.Workday;
import com.example.crudapp.model.Workplace;
import com.example.crudapp.repository.EmployeeRepository;
import com.example.crudapp.repository.WorkdayRepository;
import com.example.crudapp.repository.WorkplaceRepository;

@Service
public class WorkdayService {
    private final WorkdayRepository workdayRepository;
    private final EmployeeRepository employeeRepository;
    private final WorkplaceRepository workplaceRepository;

    public WorkdayService(WorkdayRepository workdayRepository,
                          EmployeeRepository employeeRepository,
                          WorkplaceRepository workplaceRepository) {
        this.workdayRepository = workdayRepository;
        this.employeeRepository = employeeRepository;
        this.workplaceRepository = workplaceRepository;
    }

    public ServiceResponse registerWorkday(WorkdayDTO workdayDTO) {
        try {
            // Check if a workday already exists for the same employee, workplace, and date
            Optional<Workday> existingWorkday = workdayRepository.findByEmployeeIdAndWorkplaceIdAndDate(
                    workdayDTO.getEmployeeId(),
                    workdayDTO.getWorkplaceId(),
                    workdayDTO.getDate());

            if (existingWorkday.isPresent()) {
                return new ServiceResponse(false,
                        "Workday already exists for this employee at this workplace on this date.");
            }

            // Retrieve Employee and Workplace
            Employee employee = employeeRepository.findById(workdayDTO.getEmployeeId())
                    .orElseThrow(() -> new IllegalArgumentException("Employee not found"));
            Workplace workplace = workplaceRepository.findById(workdayDTO.getWorkplaceId())
                    .orElseThrow(() -> new IllegalArgumentException("Workplace not found"));

            // Map DTO to Entity
            Workday workday = new Workday();
            workday.setEmployee(employee);
            workday.setWorkplace(workplace);
            workday.setDate(workdayDTO.getDate());
            workday.setHoursWorked((int) workdayDTO.getHoursWorked());
            workday.setOvertimeHours((int) workdayDTO.getOvertimeHours());
            workday.setTransportCost(workdayDTO.getTransportCost());

            // Save and return
            Workday savedWorkday = workdayRepository.save(workday);

            return ServiceResponse.success("Workday registered successfully", savedWorkday);

        } catch (Exception e) {
            return ServiceResponse.error("An error occurred while registering the workday: " + e.getMessage());
        }

    }

    public ByteArrayOutputStream generateWorkdayExcelReport(
            Employee employee,
            LocalDate startDate,
            LocalDate endDate,
            String workplaceFilter) throws IOException {
        List<Workday> records = workdayRepository.findByEmployeeIdAndDateBetween(employee.getId(), startDate, endDate);
        List<Workday> filteredRecords;
        if (workplaceFilter != null && !workplaceFilter.isEmpty()) {
            filteredRecords = records.stream()
                    .filter(record -> record.getWorkplace() != null &&
                                     workplaceFilter.equalsIgnoreCase(record.getWorkplace().getWorkplaceName()))
                    .collect(Collectors.toList());
        } else {
            filteredRecords = records;
        }

        WorkdayExcelExporter exporter = new WorkdayExcelExporter(filteredRecords);

        return exporter.export();
    }

    // Example of a WorkdayDTO and ServiceResponse if you have them
    // Ensure your DTOs and Models have necessary getters and setters
    // so the exporter can access the data.
    // ...

    public ServiceResponse getWorkdaysFiltered(Long employeeId, LocalDate startDate, LocalDate endDate, Long workplaceId) {
        List<Workday> workdays;

        if (startDate != null && endDate != null && workplaceId != null) {
            workdays = workdayRepository.findByEmployeeIdAndDateBetweenAndWorkplaceId(employeeId, startDate, endDate, workplaceId);
        } else if (startDate != null && endDate != null) {
            workdays = workdayRepository.findByEmployeeIdAndDateBetween(employeeId, startDate, endDate);
        } else if (workplaceId != null) {
            workdays = workdayRepository.findByEmployeeIdAndWorkplaceId(employeeId, workplaceId);
        } else {
            workdays = workdayRepository.findByEmployeeId(employeeId);
        }

        return new ServiceResponse(true, "Workdays retrieved successfully", workdays);
}

}
