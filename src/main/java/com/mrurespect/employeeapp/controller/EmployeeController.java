package com.mrurespect.employeeapp.controller;

import com.mrurespect.employeeapp.dao.EmployeeUserDTOImpl;
import com.mrurespect.employeeapp.dao.UserDao;
import com.mrurespect.employeeapp.entity.*;
import com.mrurespect.employeeapp.entity.dto.RequestDTO;
import com.mrurespect.employeeapp.security.WebUser;
import com.mrurespect.employeeapp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService employeeService;
    private final UserService userService;
    private final DepartmentService departmentService;
    private final RequestService requestService;
    private final RequesttTypeService requesttTypeService;

    private int value = -1;

    private final UserDao userDao;

    public EmployeeController(EmployeeService employeeService, UserDao userDao, UserService userService, DepartmentService departmentService, RequestService requestService, RequesttTypeService requesttTypeService) {
        this.employeeService = employeeService;
        this.userDao = userDao;
        this.userService = userService;
        this.departmentService = departmentService;
        this.requestService = requestService;
        this.requesttTypeService = requesttTypeService;
    }

    @GetMapping("/list")
    public String listEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String firstName,
            Model model,
            @Autowired Authentication authentication) {

        employeeService.listEmployees(model, authentication, firstName, page, size);
        Page<Employee> employeePage;

        // Handle search by firstName
        if (firstName != null && !firstName.isEmpty()) {
            employeePage = employeeService.searchEmployeesByFirstName(firstName, page, size); // Use paginated search
        } else {
            employeePage = employeeService.getPaginatedEmployees(page, size);
        }

        model.addAttribute("employeePage", employeePage);
        model.addAttribute("totalPages", employeePage.getTotalPages());

        return "list-employee";
}


    @GetMapping("/")
    public String redirect() {
        return "redirect:/employees/list";
    }

    @GetMapping("/addEmployee")
    public String addEmployee(Model model, @Autowired Authentication authentication) {
        employeeService.addEmployee(model, authentication);
        return "employee-form";
    }

    @PostMapping("/employees/add")
    public String addEmployee(@ModelAttribute EmployeeUserDTOImpl employeeUserDTO,
                              Model model,
                              @Autowired Authentication authentication) {

        userService.postAddEmployeeRole(model,authentication,employeeUserDTO);
        List<String> departments = departmentService.findAllNameDepartments();
        model.addAttribute("departments", departments);

        User usrname = userDao.findByUserName(employeeUserDTO.getUsername());
        if (!Objects.equals(null, usrname)) {
            model.addAttribute("registrationError", "username already exists");
            System.out.println("username already exist");
            return "employee-form";
        }

        Employee savedEmployee = employeeService.postAddEmployee(employeeUserDTO);

//        userService.postAddUser(usrname, savedEmployee, employeeUserDTO);

        return "employee-form";
    }

    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable ( value = "id") int id, Model model) {

        // get employee from the service
        Employee employee = employeeService.getEmployeeById(id);

        // set employee as a model attribute to pre-populate the form
        model.addAttribute("employee", employee);
//        List<String> departments = departmentService.findAllNameDepartments();
//        model.addAttribute("departments", departments);

        return "update_employee";
    }

    @PostMapping("/saveEmployee")
    public String saveEmployee(@ModelAttribute EmployeeUserDTOImpl employee,
                                 Model model) {
        System.out.println(employee);
        employeeService.postAddEmployee(employee);
//        List<String> departments = departmentService.findAllNameDepartments();
//        model.addAttribute("departments", departments);
//        return "redirect:/employees/list?page=" + page;
        return "update_employee";
    }

    @GetMapping("/employees")
    public List<Employee> findAll() {
        return employeeService.findAll();
    }

    @GetMapping("/employees/{employeeId}")
    public Employee findById(@PathVariable int employeeId) {
        Employee employee = employeeService.findById(employeeId).orElse(null);
        if (employee == null) {
            throw new EmployeeNotFoundException("employee id not found - " + employeeId);
        }
        return employee;
    }

    @DeleteMapping("/employees/{employeeId}")
    public String deleteEmployee(@PathVariable int employeeId) {
        Employee tempEmployee = employeeService.findById(employeeId).orElse(null);
        if (tempEmployee == null)
            throw new EmployeeNotFoundException("employee id not found - " + employeeId);
        employeeService.deleteEmployeeAndUser(employeeId);
        return "redirect:/employees/list";
    }


//    requests

    @GetMapping("/seeRequets")
    public String seeRequests(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size, Model model, @Autowired Authentication authentication ) {
        String username_user = authentication.getName(); // since username is unique
        if (username_user.equals("admin")){
            return "see_request";
        }
        Page<Request> request_page;
        User user = userService.findByUserName(username_user);
        if (user.getRole().equals("ROLE_MANAGER")) {
            List<Request> departmentRequests = requestService.getRequestsByDepartment(user.getEmployee().getDepartment_id());
            model.addAttribute("requests", departmentRequests);
            request_page = requestService.getPaginatedRequests(page, size, user.getEmployee().getDepartment_id());
        }
        else{
            request_page = requestService.getHisRequests(page, size, user.getEmployee());
        }

        model.addAttribute("changeId", value);
        model.addAttribute("requestPage", request_page); // Paginated employee data
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", request_page.getTotalPages());
        model.addAttribute(new Request());
        return "see_request";
    }

//    @GetMapping("/seeHisRequests")
//    public String SeeHisRequests(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size, Model model, @Autowired Authentication authentication ) {
//        String username_user = authentication.getName(); // since username is unique
//        if (username_user.equals("admin")){
//            return "see_request";
//        }
//
//        User user = userService.findByUserName(username_user);
////        List<Request> departmentRequests = requestService.getHisRequests( page, size, user.getEmployee());
////        model.addAttribute("requests", departmentRequests);
//        Page<Request> request_page = requestService.getHisRequests(page, size, user.getEmployee());
////
////        // Add attributes to the model
////        model.addAttribute("firstName", request_page);
////        model.addAttribute("isManager", isManager);
//        model.addAttribute("changeId", value);
//        model.addAttribute("requestPage", request_page); // Paginated employee data
//        model.addAttribute("currentPage", page);
//        model.addAttribute("totalPages", request_page.getTotalPages());
//        model.addAttribute(new Request());
//
//        return "see_request";
//    }


    @GetMapping("/createRequets")
    public String createRequests(Model model, @Autowired Authentication authentication ) {
        String username_user = authentication.getName(); // since username is unique
        if (username_user.equals("admin")){
            return "see_request";
        }

        List<String> requestTypes = requesttTypeService.findAllNameRequestType();
        model.addAttribute("requestTypes", requestTypes);
        model.addAttribute("employee", new Employee()); // Make sure you pass an instance
//        model.addAttribute("request", new Request()); // Make sure you pass an instance

        return "create_request";
    }

    @PostMapping("/employees/createRequest")
    public String createRequest(@ModelAttribute RequestDTO requestDAO,
                              Model model,
                              @Autowired Authentication authentication) {

        Employee employee_id = userDao.findByUserName(authentication.getName()).getEmployee();
        Request request_id = new Request();
        request_id.setEmployee(employee_id);
        request_id.setName(requestDAO.getName());
        request_id.setStatus(requestDAO.getStatus());
        request_id.setRequest_type(requestDAO.getRequest_type());
        request_id.setRequest_date(requestDAO.getRequest_date());
        requestService.save(request_id);
        List<String> requestTypes = requesttTypeService.findAllNameRequestType();
        model.addAttribute("requestTypes", requestTypes);
        model.addAttribute("employee", new Employee());
        return "create_request";
    }

}
