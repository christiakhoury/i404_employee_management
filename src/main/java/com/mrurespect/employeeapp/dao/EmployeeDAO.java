package com.mrurespect.employeeapp.dao;

import com.mrurespect.employeeapp.entity.Employee;

import java.util.List;

public interface EmployeeDAO {
    List<Employee> findAll();

    Employee findById(int id);

    int save(Employee employee);

    int deleteByID(int id);

    List<Employee> findByFirstName(String firstName);

}
