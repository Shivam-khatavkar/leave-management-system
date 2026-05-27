package com.company.lms.dto;

import com.company.lms.enums.LeaveType;

import lombok.Data;

@Data
public class LeaveBalanceResponseDto {

    private LeaveType leaveType;

    private int totalLeaves;

    private int remainingLeaves;
}