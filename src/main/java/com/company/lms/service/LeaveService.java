package com.company.lms.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.company.lms.dto.LeaveApprovalResponseDto;
import com.company.lms.dto.LeaveBalanceResponseDto;
import com.company.lms.dto.LeaveRequestDto;
import com.company.lms.dto.LeaveResponseDto;
import com.company.lms.entity.Employee;
import com.company.lms.entity.LeaveRequest;
import com.company.lms.enums.LeaveStatus;
import com.company.lms.enums.Role;
import com.company.lms.exception.EmployeeNotFoundException;
import com.company.lms.exception.InvalidLeaveDateException;
import com.company.lms.exception.LeaveNotFoundException;
import com.company.lms.exception.LeaveOverlapException;
import com.company.lms.repository.EmployeeRepository;
import com.company.lms.repository.LeaveRequestRepository;
import com.company.lms.util.SecurityUtil;

import java.time.temporal.ChronoUnit;

import com.company.lms.entity.LeaveBalance;
import com.company.lms.exception.InsufficientLeaveBalanceException;
import com.company.lms.repository.LeaveBalanceRepository;
import com.company.lms.enums.Role;
import org.springframework.security.access.AccessDeniedException;
import com.company.lms.util.SecurityUtil;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class LeaveService {
	
	private static final Logger logger =
	        LoggerFactory.getLogger(LeaveService.class);

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private LeaveBalanceRepository leaveBalanceRepository;

    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private SecurityUtil securityUtil;

    public LeaveResponseDto applyLeave(Long employeeId,LeaveRequestDto requestDto) {
    	
    	logger.info("Leave apply request received for employee id: {}", employeeId);
    	
    	 // SECURITY CHECK
        Long loggedInEmployeeId = securityUtil.getLoggedInEmployeeId();

        if (!loggedInEmployeeId.equals(employeeId)) {
            throw new AccessDeniedException(
                    "You cannot apply leave for another employee");
        }

        // Find employee
        Employee employee = employeeRepository.findById(employeeId)
        		
                        .orElseThrow(() ->new EmployeeNotFoundException("Employee not found with id: "+ employeeId));

        // Validate end date
        if (requestDto.getEndDate().isBefore(requestDto.getStartDate())) {

            throw new InvalidLeaveDateException("End date cannot be before start date");
        }

        // Check overlapping leaves
     List<LeaveRequest> overlappingLeaves = leaveRequestRepository.findByEmployeeAndStatusNotAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                                employee,
                                LeaveStatus.CANCELLED,
                                requestDto.getEndDate(),
                                requestDto.getStartDate()
                        );

        if (!overlappingLeaves.isEmpty()) {

            throw new LeaveOverlapException("Leave request overlaps with existing leave");
        }

     // Find leave balance

        LeaveBalance leaveBalance = leaveBalanceRepository.findByEmployeeAndLeaveType(employee,requestDto.getLeaveType())
                        .orElseThrow(() -> new RuntimeException("Leave balance not found"));


        // Calculate leave days

        long leaveDays = ChronoUnit.DAYS.between(requestDto.getStartDate(),requestDto.getEndDate()) + 1;


        // Check sufficient balance

        if(leaveBalance.getRemainingLeaves()< leaveDays) {

            throw new InsufficientLeaveBalanceException("Insufficient leave balance");
            
        }


        // Deduct leave balance

        leaveBalance.setRemainingLeaves(leaveBalance.getRemainingLeaves() - (int) leaveDays);

        leaveBalanceRepository.save(leaveBalance);
        
        // Convert DTO to Entity
        LeaveRequest leaveRequest = modelMapper.map(requestDto,LeaveRequest.class);

        // Set employee
        leaveRequest.setEmployee(employee);

        // Default status
        leaveRequest.setStatus(LeaveStatus.PENDING);

        logger.info("Saving leave request for employee: {}", employee.getEmail());
        
        // Save leave
        LeaveRequest savedLeave = leaveRequestRepository.save(leaveRequest);
        
        logger.info("Leave applied successfully with id: {}", savedLeave.getId());

        // Convert Entity to DTO
        LeaveResponseDto responseDto = modelMapper.map(savedLeave,LeaveResponseDto.class);

        responseDto.setEmployeeName(employee.getName());

        return responseDto;
    }
    
    public Page<LeaveResponseDto> getEmployeeLeaves(
            Long employeeId,
            int page,
            int size,
            String sortBy,
            String sortDir) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new EmployeeNotFoundException("Employee not found with id: " + employeeId));

        validateEmployeeAccess(employee);

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<LeaveRequest> leavePage = leaveRequestRepository.findByEmployeeId(employeeId, pageable);

        return leavePage.map(leaveRequest -> {

            LeaveResponseDto responseDto = modelMapper.map(leaveRequest, LeaveResponseDto.class);

            responseDto.setEmployeeName(employee.getName());

            return responseDto;
        });
    }
    
    public String cancelLeave(Long leaveId) {
    	
    	logger.info("Cancel leave request for leave id: {}", leaveId);

        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveId)
                        .orElseThrow(() ->
                                new LeaveNotFoundException("Leave not found with id: "+ leaveId));

        // Prevent multiple cancellation

        if (leaveRequest.getStatus()== LeaveStatus.CANCELLED) {

            throw new RuntimeException("Leave is already cancelled");
        }

        Employee employee = leaveRequest.getEmployee();

        // Find leave balance

        LeaveBalance leaveBalance = leaveBalanceRepository.findByEmployeeAndLeaveType(employee,leaveRequest.getLeaveType())
        		
                        .orElseThrow(() -> new RuntimeException("Leave balance not found"));

        // Calculate leave days

        long leaveDays = ChronoUnit.DAYS.between(leaveRequest.getStartDate(),leaveRequest.getEndDate()) + 1;

        // Restore balance

        leaveBalance.setRemainingLeaves(leaveBalance.getRemainingLeaves()+ (int) leaveDays);

        leaveBalanceRepository.save(leaveBalance);

        // Update leave status

        leaveRequest.setStatus(LeaveStatus.CANCELLED);

        leaveRequestRepository.save(leaveRequest);

        return "Leave cancelled successfully";
    }
    
    public List<LeaveBalanceResponseDto> getEmployeeLeaveBalance(Long employeeId) {

        Employee employee = employeeRepository.findById(employeeId)
                        .orElseThrow(() ->new EmployeeNotFoundException("Employee not found with id: "+ employeeId));

        List<LeaveBalance> leaveBalances = leaveBalanceRepository
                        .findAll()
                        .stream()
                        .filter(balance ->balance.getEmployee()
                                        .getId()
                                        .equals(employeeId))
                                        .toList();

        return leaveBalances.stream()
                .map(balance -> modelMapper.map(balance,LeaveBalanceResponseDto.class))
                .toList();
        
    }
    
    public LeaveApprovalResponseDto approveLeave(Long leaveId) {
    	
    	logger.info("Manager approving leave id: {}", leaveId);

        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveId)
                        .orElseThrow(() ->
                                new LeaveNotFoundException("Leave not found with id: "+ leaveId));

        // Allow only pending leave

        if (leaveRequest.getStatus()!= LeaveStatus.PENDING) {

            throw new RuntimeException("Only pending leave can be approved");
            
        }

        // Update status

        leaveRequest.setStatus(LeaveStatus.APPROVED);

        LeaveRequest updatedLeave = leaveRequestRepository.save(leaveRequest);

        // Prepare response

        LeaveApprovalResponseDto response = new LeaveApprovalResponseDto();

        response.setLeaveId(updatedLeave.getId());

        response.setEmployeeName(updatedLeave.getEmployee().getName());

        response.setStatus(updatedLeave.getStatus());

        response.setMessage("Leave approved successfully");
        
        logger.info("Leave approved successfully: {}", leaveId);

        return response;
    }
    
    public LeaveApprovalResponseDto rejectLeave(Long leaveId) {
    	
    	logger.info("Manager rejecting leave id: {}", leaveId);

        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveId)
                        .orElseThrow(() ->
                                new LeaveNotFoundException("Leave not found with id: "+ leaveId));

        // Allow only pending leave

        if (leaveRequest.getStatus()!= LeaveStatus.PENDING) {

            throw new RuntimeException("Only pending leave can be rejected");
            
        }

        Employee employee = leaveRequest.getEmployee();

        // Find leave balance

        LeaveBalance leaveBalance = leaveBalanceRepository
                        .findByEmployeeAndLeaveType(
                                employee,
                                leaveRequest.getLeaveType()
                        )
                        .orElseThrow(() ->
                                new RuntimeException("Leave balance not found"));

        // Calculate leave days

        long leaveDays = ChronoUnit.DAYS.between(leaveRequest.getStartDate(),leaveRequest.getEndDate()) + 1;

        // Restore leave balance

        leaveBalance.setRemainingLeaves(leaveBalance.getRemainingLeaves()+ (int) leaveDays);

        leaveBalanceRepository.save(leaveBalance);

        // Update leave status

        leaveRequest.setStatus(LeaveStatus.REJECTED);

        LeaveRequest updatedLeave = leaveRequestRepository.save(leaveRequest);

        // Prepare response

        LeaveApprovalResponseDto response = new LeaveApprovalResponseDto();

        response.setLeaveId(updatedLeave.getId());

        response.setEmployeeName(updatedLeave.getEmployee().getName());

        response.setStatus(updatedLeave.getStatus());

        response.setMessage("Leave rejected successfully");
        
        logger.info("Leave rejected successfully: {}", leaveId);

        return response;
    }
    
    public List<LeaveResponseDto> getPendingLeaves() {

        List<LeaveRequest> pendingLeaves = leaveRequestRepository.findByStatus(LeaveStatus.PENDING);

        return pendingLeaves.stream()
                .map(leaveRequest -> {

                    LeaveResponseDto responseDto = modelMapper.map(leaveRequest,LeaveResponseDto.class);

                    responseDto.setEmployeeName(leaveRequest.getEmployee().getName());

                    return responseDto;
                })
                .toList();
        
    }
    
  
    
    private void validateEmployeeAccess(Employee employee) {

        String loggedInEmail =
                SecurityUtil.getLoggedInUserEmail();

        Employee loggedInEmployee =
                employeeRepository.findByEmail(loggedInEmail)
                        .orElseThrow(() ->
                                new EmployeeNotFoundException(
                                        "Logged in employee not found"));

        // Manager/Admin can access all

        if (loggedInEmployee.getRole() == Role.MANAGER ||
                loggedInEmployee.getRole() == Role.ADMIN) {

            return;
        }

        // Employee can access only own data

        if (!loggedInEmployee.getId().equals(employee.getId())) {

            throw new AccessDeniedException(
                    "You cannot access another employee data");
        }
    }
    
}