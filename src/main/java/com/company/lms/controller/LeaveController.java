package com.company.lms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.company.lms.dto.LeaveApprovalResponseDto;
import com.company.lms.dto.LeaveBalanceResponseDto;
import com.company.lms.dto.LeaveRequestDto;
import com.company.lms.dto.LeaveResponseDto;
import com.company.lms.service.LeaveService;

import jakarta.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/leaves")
public class LeaveController {

    @Autowired
    private LeaveService leaveService;

    
    @PreAuthorize("hasAnyRole('EMPLOYEE','MANAGER','ADMIN')")
    @PostMapping("/apply/{employeeId}")
    public LeaveResponseDto applyLeave( @PathVariable Long employeeId, @Valid @RequestBody LeaveRequestDto requestDto) {

        return leaveService.applyLeave(employeeId,requestDto);
        
    }
    
    @PreAuthorize("hasAnyRole('EMPLOYEE','MANAGER','ADMIN')")
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<Page<LeaveResponseDto>> getEmployeeLeaves(

            @PathVariable Long employeeId,

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "5") int size,

            @RequestParam(defaultValue = "startDate") String sortBy,

            @RequestParam(defaultValue = "desc") String sortDir) {

        return ResponseEntity.ok(
                leaveService.getEmployeeLeaves(
                        employeeId,
                        page,
                        size,
                        sortBy,
                        sortDir
                ));
    }
    
    @PreAuthorize("hasAnyRole('EMPLOYEE','MANAGER','ADMIN')")
    @PutMapping("/cancel/{leaveId}")
    public String cancelLeave(@PathVariable Long leaveId) {

        return leaveService.cancelLeave(leaveId);
        
    }
    
    @PreAuthorize("hasAnyRole('EMPLOYEE','MANAGER','ADMIN')")
    @GetMapping("/balance/{employeeId}")
    public List<LeaveBalanceResponseDto> getEmployeeLeaveBalance(@PathVariable Long employeeId) {

        return leaveService.getEmployeeLeaveBalance(employeeId);
        
    }
    
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    @PutMapping("/approve/{leaveId}")
    public LeaveApprovalResponseDto approveLeave(@PathVariable Long leaveId) {

        return leaveService.approveLeave(leaveId);
        
    }
    
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    @PutMapping("/reject/{leaveId}")
    public LeaveApprovalResponseDto rejectLeave(@PathVariable Long leaveId) {

        return leaveService.rejectLeave(leaveId);
        
    }
    
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    @GetMapping("/pending")
    public List<LeaveResponseDto> getPendingLeaves() {

        return leaveService.getPendingLeaves();
        
    }
    
}