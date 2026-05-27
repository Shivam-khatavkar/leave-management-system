package com.company.lms.dto;

import com.company.lms.enums.LeaveStatus;

import lombok.Data;

@Data
public class LeaveApprovalResponseDto {

    private Long leaveId;

    private String employeeName;

    private LeaveStatus status;

    private String message;
}