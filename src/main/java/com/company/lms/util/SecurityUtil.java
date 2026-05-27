package com.company.lms.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.company.lms.entity.Employee;
import com.company.lms.repository.EmployeeRepository;

@Component
public class SecurityUtil {
	
	@Autowired
	private EmployeeRepository employeeRepository;

    public static String getLoggedInUserEmail() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        return authentication.getName();
  
}
    

    public Long getLoggedInEmployeeId() {

        String email = getLoggedInUserEmail();

        Employee employee = employeeRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Employee not found"));

        return employee.getId();
    }
    
    }