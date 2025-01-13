package com.mrurespect.employeeapp.service;

import com.mrurespect.employeeapp.dao.*;
import com.mrurespect.employeeapp.dao.rowMapper.EmployeeMapper;
import com.mrurespect.employeeapp.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final RoleDao  roleDao;

    public EmployeeServiceImpl(EmployeeRepository theEmployeeRepository,
                               DepartmentDAO departmentDAO, DepartmentService departmentService,
                               JdbcTemplate jdbcTemplate, UserServiceImpl userServiceImpl, RoleDao roleDao) {

        employeeRepository = theEmployeeRepository;
        this.departmentDAO = departmentDAO;
        this.departmentService = departmentService;
        this.jdbcTemplate = jdbcTemplate;
        this.userServiceImpl = userServiceImpl;
        this.roleDao = roleDao;
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
    public void listEmployees(Model model, Authentication authentication, String firstName, int page, int size) {
        // Determine roles
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> "ROLE_ADMIN".equals(auth.getAuthority()));
        boolean isManager = authentication.getAuthorities().stream()
                .anyMatch(auth -> "ROLE_MANAGER".equals(auth.getAuthority()));

        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("isManager", isManager);
        model.addAttribute("currentPage", page);
        model.addAttribute("firstName", firstName);  // Preserve the search term in the input field
        model.addAttribute(new Employee());  // Create a new Employee object for the form
    }

    @Override
    public Employee postAddEmployee(EmployeeUserDTOImpl employeeUserDTO, String action_type, int id) {
        // Create Employee
        Employee theEmployee = new Employee();
        theEmployee.setFirstName(employeeUserDTO.getFirstName());
        theEmployee.setLastName(employeeUserDTO.getLastName());
        theEmployee.setEmail(employeeUserDTO.getEmail());
        theEmployee.setDepartment(employeeUserDTO.getDepartment());

        // Save Employee to DB
        Employee employee;
        if (!Objects.equals(null, theEmployee)){
            employee = theEmployee;
        }
        else {
            employee = new Employee();
        }

        if (id > -1){
            employee.setId(id);
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
        if (theEmployee.getDepartment().equals("SALES")) {
            employee.setDepartment_id(departmentDAO.findDepartmentByName("SALES"));
        }

        Employee employeessss = employeeRepository.save(employee);

        if (action_type.equals("create")) {
            WebUser user = new WebUser();
            user.setUsername(employeeUserDTO.getUsername());
            user.setPassword(employeeUserDTO.getPassword());
            user.setEmployee(employeessss);
            user.setRole(employeeUserDTO.getRole());

            User new_user = userServiceImpl.save(user);
//            String rolesSql = "INSERT INTO users_roles (user_id, role_id) VALUES (?, ?)";
//
//            String role = "ROLE_EMPLOYEE";
//            if (new_user.getRole().equals("ADMIN")){
//                role = "ROLE_ADMIN";
//            } else if (new_user.getRole().equals("MANAGER")) {
//                role = "ROLE_MANAGER";
//            }
//            else{
//                role = "ROLE_EMPLOYEE";
//            }
//            Role role_id = roleDao.findRoleByName(role);
//            User usr_id = userServiceImpl.findByUserName1(new_user.getUserName());
//            jdbcTemplate.update(rolesSql, usr_id.getId(), role_id.getId());

        }
        return employeessss;
//        return savedEmployee;
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





