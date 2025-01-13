package com.mrurespect.employeeapp.service;

import com.mrurespect.employeeapp.dao.EmployeeUserDTOImpl;
import com.mrurespect.employeeapp.dao.rowMapper.EmployeeMapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.mrurespect.employeeapp.dao.DepartmentDAO;
import com.mrurespect.employeeapp.dao.EmployeeRepository;
import com.mrurespect.employeeapp.dao.RoleDao;
import com.mrurespect.employeeapp.entity.Employee;
import com.mrurespect.employeeapp.entity.User;
import com.mrurespect.employeeapp.security.WebUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentDAO departmentDAO;
    private final DepartmentService departmentService;
    private final JdbcTemplate jdbcTemplate;
    private final UserServiceImpl userServiceImpl;
    private final EmployeeMapper employeeMapper = new EmployeeMapper();

    public EmployeeServiceImpl(EmployeeRepository theEmployeeRepository,
                               DepartmentDAO departmentDAO, DepartmentService departmentService,
                               JdbcTemplate jdbcTemplate, UserServiceImpl userServiceImpl) {

        employeeRepository = theEmployeeRepository;
        this.departmentDAO = departmentDAO;
        this.departmentService = departmentService;
        this.jdbcTemplate = jdbcTemplate;
        this.userServiceImpl = userServiceImpl;
    }



    public Page<Employee> getPaginatedEmployees(int page, int size) {
        return employeeRepository.findAll(PageRequest.of(page, size, Sort.by("id").ascending()));
    }

    @Override
    public Page<Employee> searchEmployeesByFirstName(String firstName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending()); // Adjust sorting as needed
        return employeeRepository.findByFirstName(firstName, pageable);
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

    @Override
    public void addEmployee(Model model, Authentication authentication) {
        model.addAttribute(new Employee());
        List<String> roles;
        if (authentication.getAuthorities().stream().anyMatch(auth -> "ROLE_ADMIN".equals(auth.getAuthority()))){
            roles = Arrays.asList("ADMIN", "MANAGER", "EMPLOYEE");
        }
        else {
            roles = Arrays.asList("MANAGER", "EMPLOYEE");
        }
        model.addAttribute("roles", roles);
        List<String> departments = departmentService.findAllNameDepartments();
        model.addAttribute("departments", departments);
    }

//    @Override
//    public List<Employee> findAll() {
//        return employeeRepository.findAll();
//    }

    public List<Employee> findAll() {
        String sql = "SELECT * FROM employees";

        return jdbcTemplate.query(sql, employeeMapper);
    }


    @Override
    public Optional<Employee> findById(int theId) {

        if (theId <= 0) {
            return Optional.empty();
        }
        return employeeRepository.findById(theId);
    }


//    @Override
//    public Employee save(Employee theEmployee) {
//
//        return employeeRepository.save(theEmployee);
//    }

//    @Override
//    @Transactional
//    public Employee save(Employee theEmployee) {
//
//    }

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





