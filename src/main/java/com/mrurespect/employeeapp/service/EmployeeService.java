package com.mrurespect.employeeapp.service;


import com.mrurespect.employeeapp.dao.EmployeeUserDTOImpl;
import com.mrurespect.employeeapp.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    List<Employee> findAll();

    Optional<Employee> findById(int theId);

//    Employee save(Employee theEmployee);

    void deleteById(int theId);

    Employee getEmployeeById(int id);

    Page<Employee> getPaginatedEmployees(int page, int size);

    void deleteEmployeeAndUser(int employeeId);

    void addEmployee(Model model, Authentication authentication);

    Page<Employee> searchEmployeesByFirstName(String firstName, int page, int size);

    void listEmployees(Model model, Authentication authentication, String firstName, int page, int size);

    Employee postAddEmployee(EmployeeUserDTOImpl employeeUserDTO);
}
