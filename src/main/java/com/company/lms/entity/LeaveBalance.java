package com.company.lms.entity;

import com.company.lms.enums.LeaveType;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class LeaveBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private LeaveType leaveType;

    private int totalLeaves;

    private int remainingLeaves;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
}