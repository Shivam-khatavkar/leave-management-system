package com.company.lms.dto;

import java.time.LocalDate;

import com.company.lms.enums.LeaveStatus;
import com.company.lms.enums.LeaveType;

import lombok.Data;

@Data
public class LeaveResponseDto {

    private Long id;

    private String employeeName;

    private LeaveType leaveType;

    private LocalDate startDate;

    private LocalDate endDate;

    private String reason;

    private LeaveStatus status;
}