package com.company.lms.repository;

import com.company.lms.entity.Employee;
import com.company.lms.entity.LeaveRequest;
import com.company.lms.enums.LeaveStatus;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.company.lms.entity.LeaveRequest;
import com.company.lms.enums.LeaveStatus;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
	
	List<LeaveRequest> findByEmployee(Employee employee);
	
	List<LeaveRequest> findByEmployeeAndStatusNotAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
	        Employee employee,
	        LeaveStatus status,
	        LocalDate endDate,
	        LocalDate startDate
	);
	
	List<LeaveRequest> findByStatus(LeaveStatus status);
	
	Page<LeaveRequest> findByEmployeeId(
	        Long employeeId,
	        Pageable pageable
	);
	
}