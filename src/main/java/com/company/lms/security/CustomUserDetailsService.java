package com.company.lms.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.authority.
SimpleGrantedAuthority;

import org.springframework.security.core.userdetails.
User;

import org.springframework.security.core.userdetails.
UserDetails;

import org.springframework.security.core.userdetails.
UserDetailsService;

import org.springframework.security.core.userdetails.
UsernameNotFoundException;

import org.springframework.stereotype.Service;

import com.company.lms.entity.Employee;
import com.company.lms.repository.EmployeeRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Employee employee = employeeRepository
                        .findByEmail(email)

                        .orElseThrow(() ->
                                new UsernameNotFoundException("User not found"));

        return new User(

                employee.getEmail(),

                employee.getPassword(),

                List.of(
                        new SimpleGrantedAuthority("ROLE_"+ employee.getRole().name())
                       )
         );
    }
}