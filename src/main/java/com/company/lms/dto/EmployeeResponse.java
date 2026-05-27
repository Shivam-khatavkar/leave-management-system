package com.company.lms.dto;

import com.company.lms.enums.Role;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EmployeeResponse {

    private Long id;

    private String name;

    private String email;

    private String department;

    private LocalDate joiningDate;

    private Role role;
}