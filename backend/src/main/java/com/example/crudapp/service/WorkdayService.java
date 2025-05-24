package com.example.crudapp.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.example.crudapp.dto.ServiceResponse;
import com.example.crudapp.dto.WorkdayDTO;
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

    public ByteArrayOutputStream generateWorkdayExcelReport(Employee employee, LocalDate startDate, LocalDate endDate)
            throws IOException {
        List<Workday> workdays = workdayRepository.findByEmployeeIdAndDateBetween(employee.getId(), startDate, endDate);

        List<WorkdayDTO> workdayDTOs = workdays.stream()
                .map(workday -> new WorkdayDTO(
                        workday.getDate(),
                        workday.getWorkplace().getWorkplaceName(),
                        workday.getHoursWorked(),
                        workday.getOvertimeHours(),
                        workday.getTransportCost()))
                .collect(Collectors.toList());

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Workdays");

        Row headerRow = (sheet).createRow(0);
        headerRow.createCell(0).setCellValue("Date");
        headerRow.createCell(1).setCellValue("Workplace");
        headerRow.createCell(2).setCellValue("Hours Worked");
        headerRow.createCell(3).setCellValue("Overtime Hours");
        headerRow.createCell(4).setCellValue("Transport Cost");

        int rowNum = 1;
        for (WorkdayDTO workdayDTO : workdayDTOs) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(workdayDTO.getDate().toString());
            row.createCell(1).setCellValue(workdayDTO.getWorkplaceName());
            row.createCell(2).setCellValue(workdayDTO.getHoursWorked());
            row.createCell(3).setCellValue(workdayDTO.getOvertimeHours());
            row.createCell(4).setCellValue(workdayDTO.getTransportCost());
        }

        for (int i = 0; i < 5; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();
        return out;
    }

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
