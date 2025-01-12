package com.mrurespect.employeeapp.service;


import com.mrurespect.employeeapp.dao.DepartmentDAO;
import com.mrurespect.employeeapp.dao.EmployeeRepository;
import com.mrurespect.employeeapp.dao.RoleDao;
import com.mrurespect.employeeapp.entity.Employee;
import com.mrurespect.employeeapp.entity.User;
import com.mrurespect.employeeapp.security.WebUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentDAO departmentDAO;

    public EmployeeServiceImpl(EmployeeRepository theEmployeeRepository, DepartmentDAO departmentDAO, JdbcTemplate jdbcTemplate) {
        employeeRepository = theEmployeeRepository;
        this.departmentDAO = departmentDAO;
        this.jdbcTemplate = jdbcTemplate;
    }
    private final JdbcTemplate jdbcTemplate;


    public Page<Employee> getPaginatedEmployees(int page, int size) {
        return employeeRepository.findAll(PageRequest.of(page, size, Sort.by("id").ascending()));
    }

    @Override
    public void deleteEmployeeAndUser(int employeeId) {
        // Delete the user first, because the Employee depends on the User (assuming foreign key constraints)
        String deleteUserSql = "DELETE FROM uuser WHERE employee_id = ?";
        jdbcTemplate.update(deleteUserSql, employeeId);  // Assuming user table has a field 'id' corresponding to employee's user_id

        // Then, delete the employee
        String deleteEmployeeSql = "DELETE FROM employees WHERE id = ?";
        jdbcTemplate.update(deleteEmployeeSql, employeeId);  // Deleting the employee using the same ID (assuming it's the same ID for both)

        System.out.println("User and Employee deleted for ID: " + employeeId);
    }

//    @Override
//    public List<Employee> findAll() {
//        return employeeRepository.findAll();
//    }

    public List<Employee> findAll() {
        String sql = "SELECT * FROM employees";

        return jdbcTemplate.query(sql, employeeRowMapper());
    }

    private RowMapper<Employee> employeeRowMapper() {
        return (rs, rowNum) -> {
            Employee employee = new Employee();
            employee.setId(rs.getInt("id")); // Assuming the column name is 'id'
            employee.setFirstName(rs.getString("first_name")); // Assuming the column name is 'first_name'
            employee.setLastName(rs.getString("last_name")); // Assuming the column name is 'last_name'
            employee.setEmail(rs.getString("email")); // Assuming the column name is 'email'
            return employee;
        };
    }

    @Override
    public Optional<Employee> findById(int theId) {

        return employeeRepository.findById(theId);
    }

//    @Override
//    public Employee save(Employee theEmployee) {
//
//        return employeeRepository.save(theEmployee);
//    }

    @Override
    @Transactional
    public Employee save(Employee theEmployee) {
        Employee employee;
        if (!Objects.equals(null, theEmployee)){
            employee = theEmployee;
        }
        else {
            employee = new Employee();
        }

        employee.setEmail(theEmployee.getEmail());
        employee.setFirstName(theEmployee.getFirstName());
        employee.setLastName(theEmployee.getLastName());
        employee.setDepartment(theEmployee.getDepartment());

        if (theEmployee.getDepartment().equals("HR")) {
            employee.setDepartment_id(departmentDAO.findDepartmentByName("HR"));
        }
        if (theEmployee.getDepartment().equals("DEVELOPMENT")) {
            employee.setDepartment_id(departmentDAO.findDepartmentByName("DEVELOPMENT"));
        }
        if (theEmployee.getDepartment().equals("NETWORKING")) {
            employee.setDepartment_id(departmentDAO.findDepartmentByName("NETWORKING"));
        }
        if (theEmployee.getDepartment().equals("IT")) {
            employee.setDepartment_id(departmentDAO.findDepartmentByName("IT"));
        }
        employeeRepository.save(employee);
        return employee;
    }

    @Override
    public void deleteById(int theId) {
        employeeRepository.deleteById(theId);
    }

//    @Transactional
//    public void deleteEmployeeAndUser(Long employeeId) {
//        // Delete the user first, because the Employee depends on the User (assuming foreign key constraints)
//        String deleteUserSql = "DELETE FROM uuser WHERE id = ?";
//        jdbcTemplate.update(deleteUserSql, employeeId);  // Assuming user table has a field 'id' corresponding to employee's user_id
//
//        // Then, delete the employee
//        String deleteEmployeeSql = "DELETE FROM employees WHERE id = ?";
//        jdbcTemplate.update(deleteEmployeeSql, employeeId);  // Deleting the employee using the same ID (assuming it's the same ID for both)
//
//        System.out.println("User and Employee deleted for ID: " + employeeId);
//    }

    @Override
    public Employee getEmployeeById(int id) {
        Optional<Employee> optional = employeeRepository.findById(id);
        Employee employee = null;
        if (optional.isPresent()) {
            employee = optional.get();
        } else {
            throw new RuntimeException(" Employee not found for id :: " + id);
        }
        return employee;
    }
}





