package com.company.lms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.company.lms.dto.LoginRequest;
import com.company.lms.dto.LoginResponse;
import com.company.lms.entity.Employee;
import com.company.lms.repository.EmployeeRepository;
import com.company.lms.service.JwtService;

@Service
public class AuthService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtService jwtService;

    public LoginResponse login(LoginRequest request) {

        Employee employee = employeeRepository.findByEmail(request.getEmail().trim().toLowerCase())
                        .orElseThrow(() ->
                                new RuntimeException("Invalid email or password"));

        // Check password

        boolean passwordMatches = passwordEncoder.matches(request.getPassword(),employee.getPassword());

        if (!passwordMatches) {

            throw new RuntimeException("Invalid email or password");
            
        }

        String token = jwtService.generateToken(employee.getEmail());

        return new LoginResponse("Login successful",token,employee.getRole().name());
        
    }
}