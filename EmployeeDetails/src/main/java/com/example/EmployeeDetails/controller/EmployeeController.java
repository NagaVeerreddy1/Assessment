package com.example.EmployeeDetails.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.EmployeeDetails.Model.Employee;
import com.example.EmployeeDetails.Model.EmployeeTaxDeductionDTO;
import com.example.EmployeeDetails.service.EmployeeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/employees")
@Validated
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<Employee> saveEmployee(@Valid @RequestBody Employee employee) {
        Employee savedEmployee = employeeService.saveEmployee(employee);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }

    @GetMapping("/tax-deductions")
    public ResponseEntity<List<EmployeeTaxDeductionDTO>> getEmployeesTaxDeductions() {
        List<EmployeeTaxDeductionDTO> deductions = employeeService.getEmployeesTaxDeductions();
        return new ResponseEntity<>(deductions, HttpStatus.OK);
    }
}
