package com.company.lms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.company.lms.entity.Employee;
import com.company.lms.entity.LeaveBalance;
import com.company.lms.enums.LeaveType;

public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Long> {

    Optional<LeaveBalance> findByEmployeeAndLeaveType(Employee employee,LeaveType leaveType);
    
    
    
}