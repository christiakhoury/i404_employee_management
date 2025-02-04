package com.mrurespect.employeeapp.dao;

import com.mrurespect.employeeapp.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    Page<Employee> findAll(Pageable pageable);
    Page<Employee> findByFirstName(String firstName, Pageable pageable);
}
