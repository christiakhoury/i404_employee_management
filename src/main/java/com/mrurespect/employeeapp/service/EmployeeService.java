package com.mrurespect.employeeapp.service;


import com.mrurespect.employeeapp.entity.Employee;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    List<Employee> findAll();

    Optional<Employee> findById(int theId);

    Employee save(Employee theEmployee);

    void deleteById(int theId);

    Employee getEmployeeById(int id);

    Page<Employee> getPaginatedEmployees(int page, int size);

    void deleteEmployeeAndUser(int employeeId);
}
