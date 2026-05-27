package com.company.lms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.company.lms.dto.EmployeeResponse;
import com.company.lms.dto.RegisterRequest;
import com.company.lms.service.EmployeeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/register")
    public EmployeeResponse registerEmployee(
           @Valid @RequestBody RegisterRequest request) {

        return employeeService.registerEmployee(request);
    }
    
    @GetMapping
    public List<EmployeeResponse> getAllEmployees() {

        return employeeService.getAllEmployees();
    }
    
    @GetMapping("/{id}")
    public EmployeeResponse getEmployeeById(
            @PathVariable Long id) {

        return employeeService.getEmployeeById(id);
    }
    
    @PutMapping("/{id}")
    public EmployeeResponse updateEmployee(
           @PathVariable Long id,
          @Valid  @RequestBody RegisterRequest request) {

        return employeeService.updateEmployee(id, request);
    }
    
    @DeleteMapping("/{id}")
    public String deleteEmployee(
            @PathVariable Long id) {

        return employeeService.deleteEmployee(id);
    }
    
}