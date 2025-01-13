package com.mrurespect.employeeapp.dao;

import com.mrurespect.employeeapp.entity.Department;
import com.mrurespect.employeeapp.entity.Employee;
import com.mrurespect.employeeapp.entity.Request;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository  extends JpaRepository<Request, Integer> {

    Page<Request> findByStatus(String status, Pageable pageable);

//    Page<Request> findByDepartmentIdAndStatus(Department departmentId, String status, Pageable pageable);

    Page<Request> findByEmployeeAndStatus(Employee employee, String status, Pageable pageable);
}