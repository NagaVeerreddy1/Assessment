package com.example.EmployeeDetails.service;


import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.EmployeeDetails.Model.Employee;
import com.example.EmployeeDetails.Model.EmployeeTaxDeductionDTO;
import com.example.EmployeeDetails.repository.EmployeeRepository;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public List<EmployeeTaxDeductionDTO> getEmployeesTaxDeductions() {
        List<Employee> employees = employeeRepository.findAll();
        List<EmployeeTaxDeductionDTO> deductions = new ArrayList<>();

        LocalDate startOfFinancialYear = LocalDate.now().withMonth(4).withDayOfMonth(1);
        LocalDate endOfFinancialYear = LocalDate.now().withMonth(3).withDayOfMonth(31);

        for (Employee employee : employees) {
            // Calculate the number of days the employee worked during the financial year
            LocalDate doj = employee.getDoj();
            if (doj.isAfter(endOfFinancialYear)) {
                continue; // Skip employees who didn't work during the financial year
            }

            LocalDate actualStart = doj.isBefore(startOfFinancialYear) ? startOfFinancialYear : doj;
            long daysWorked = ChronoUnit.DAYS.between(actualStart, endOfFinancialYear.plusDays(1));
            double monthsWorked = daysWorked / 30.0;

            // Calculate yearly salary and taxes
            double monthlySalary = employee.getSalary();
            double totalSalary = monthlySalary * monthsWorked;

            double tax = calculateTax(totalSalary);
            double cess = calculateCess(totalSalary);

            // Create a DTO for the employee's tax deductions
            EmployeeTaxDeductionDTO dto = new EmployeeTaxDeductionDTO(
                employee.getEmployeeId(),
                employee.getFirstName(),
                employee.getLastName(),
                totalSalary,
                tax,
                cess
            );
            deductions.add(dto);
        }

        return deductions;
    }

    private double calculateTax(double totalSalary) {
        double tax = 0;

        if (totalSalary <= 250000) {
            tax = 0;
        } else if (totalSalary > 250000 && totalSalary <= 500000) {
            tax = (totalSalary - 250000) * 0.05;
        } else if (totalSalary > 500000 && totalSalary <= 1000000) {
            tax = (250000 * 0.05) + ((totalSalary - 500000) * 0.10);
        } else {
            tax = (250000 * 0.05) + (500000 * 0.10) + ((totalSalary - 1000000) * 0.20);
        }

        return tax;
    }

    private double calculateCess(double totalSalary) {
        if (totalSalary > 2500000) {
            return (totalSalary - 2500000) * 0.02;
        }
        return 0;
    }
}
