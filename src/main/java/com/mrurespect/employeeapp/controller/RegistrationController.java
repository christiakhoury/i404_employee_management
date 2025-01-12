package com.mrurespect.employeeapp.controller;

import com.mrurespect.employeeapp.dao.EmployeeUserDTOImpl;
import com.mrurespect.employeeapp.entity.Employee;
import com.mrurespect.employeeapp.entity.User;
import com.mrurespect.employeeapp.security.WebUser;
import com.mrurespect.employeeapp.service.EmployeeService;
import com.mrurespect.employeeapp.service.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.apache.catalina.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/register")
public class RegistrationController {
    private final UserService userService;
    private final EmployeeService employeeService;

    @Autowired
    public RegistrationController(UserService userService, EmployeeService employeeService) {
        this.userService = userService;
        this.employeeService = employeeService;
    }

    @GetMapping("/showRegisterForm")
    public String register(Model theModel) {
        theModel.addAttribute("webUser", new WebUser());
        List<String> roles = Arrays.asList("ADMIN", "MANAGER", "EMPLOYEE");
        theModel.addAttribute("roles", roles);
        theModel.addAttribute("user", new User());
        return "register";
    }

    //This code performs validation, checks the database to see if the user already exists,
    //creates user and stores in database and finally place in the session for later use.
    @PostMapping("/processRegistrationForm")
    public String register(@ModelAttribute("webUser") @Valid WebUser webUser,
                           BindingResult bindingResult,
                           HttpSession session,
                           Model model) {
        System.out.println("processing registration form for " + webUser);

        //form validation
        if (bindingResult.hasErrors()) return "register";

        //check the database if the user already exists
        User existing = userService.findByUserName(webUser.getUsername());
        if (!Objects.equals(null, existing)) {
            model.addAttribute("registrationError", "username already exists");
            System.out.println("username already exist");
            return "register";
        }
        Employee employee = new Employee();
        employee.setFirstName(webUser.getFirstName());
        employee.setLastName(webUser.getLastName());
        employee.setEmail(webUser.getEmail());

        // Save Employee to DB
        Employee savedEmployee = employeeService.save(employee);
        webUser.setEmployee(savedEmployee);
        //create user account and store it in the database
        userService.save(webUser);
        System.out.println("successfully created " + webUser);

        //place the user in the http session for later use
        session.setAttribute("user", webUser);
        return "register-confirmation";
    }
    //The UserService provides access to the database via the UserDao for creating
    //users. We'll also use this bean to check if a user exists.

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
        //The @InitBinder is code that we've used before. It is used in the form validation process.
        // Here we add support to trim empty strings to null
    }
}