package com.company.lms.service;

import com.company.lms.dto.EmployeeResponse;
import com.company.lms.dto.RegisterRequest;
import com.company.lms.entity.Employee;
import com.company.lms.exception.DuplicateEmailException;
import com.company.lms.exception.EmployeeNotFoundException;
import com.company.lms.repository.EmployeeRepository;
import com.company.lms.repository.LeaveBalanceRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.company.lms.entity.LeaveBalance;
import com.company.lms.enums.LeaveType;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private LeaveBalanceRepository leaveBalanceRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    public EmployeeResponse registerEmployee(RegisterRequest request) {
    	
    	String email = request.getEmail().trim().toLowerCase();

        boolean exists = employeeRepository.findByEmail(email).isPresent();

        if(exists) {
            throw new DuplicateEmailException("Email already exists");
            
        }

        Employee employee = modelMapper.map(request, Employee.class);

        employee.setJoiningDate(LocalDate.now());
        
        employee.setEmail(email);
        
        employee.setPassword(passwordEncoder.encode(request.getPassword()));

        Employee savedEmployee = employeeRepository.save(employee);
        
     // Create default leave balances

        createLeaveBalance(
                savedEmployee,
                LeaveType.SICK,
                10);

        createLeaveBalance(
                savedEmployee,
                LeaveType.CASUAL,
                8);

        createLeaveBalance(
                savedEmployee,
                LeaveType.PAID,
                15);

        return modelMapper.map(savedEmployee,EmployeeResponse.class);
        
    }
    
    private void createLeaveBalance(Employee employee,LeaveType leaveType,int totalLeaves) {

        LeaveBalance leaveBalance = new LeaveBalance();

        leaveBalance.setEmployee(employee);

        leaveBalance.setLeaveType(leaveType);

        leaveBalance.setTotalLeaves(totalLeaves);

        leaveBalance.setRemainingLeaves(totalLeaves);

        leaveBalanceRepository.save(leaveBalance);
        
    }
    
    public List<EmployeeResponse> getAllEmployees() {

        List<Employee> employees =
                employeeRepository.findAll();

        return employees.stream()
                .map(employee ->
                        modelMapper.map(employee,
                                EmployeeResponse.class))
                .collect(Collectors.toList());
    }
    
    
    	public EmployeeResponse getEmployeeById(Long id) {

    	    Employee employee =
    	            employeeRepository.findById(id)
    	                    .orElseThrow(() ->
    	                            new EmployeeNotFoundException(
    	                                    "Employee not found with id: " + id
    	                            ));

    	    return modelMapper.map(employee,
    	            EmployeeResponse.class);
    	}
    
    	public EmployeeResponse updateEmployee(
    	        Long id,
    	        RegisterRequest request) {

    	    Employee employee =
    	            employeeRepository.findById(id)
    	                    .orElseThrow(() ->
    	                            new EmployeeNotFoundException(
    	                                    "Employee not found with id: " + id
    	                            ));

    	    employee.setName(request.getName());
    	    employee.setEmail(request.getEmail());
    	    employee.setPassword(request.getPassword());
    	    employee.setDepartment(request.getDepartment());
    	    employee.setRole(request.getRole());

    	    Employee updatedEmployee =
    	            employeeRepository.save(employee);

    	    return modelMapper.map(updatedEmployee,
    	            EmployeeResponse.class);
    	}
    
    	public String deleteEmployee(Long id) {

    	    Employee employee =
    	            employeeRepository.findById(id)
    	                    .orElseThrow(() ->
    	                            new EmployeeNotFoundException(
    	                                    "Employee not found with id: " + id
    	                            ));

    	    employeeRepository.delete(employee);

    	    return "Employee deleted successfully";
    	}
    
}