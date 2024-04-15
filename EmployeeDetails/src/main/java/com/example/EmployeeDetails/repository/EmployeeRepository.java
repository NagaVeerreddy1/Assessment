package com.example.EmployeeDetails.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.EmployeeDetails.Model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    
}
