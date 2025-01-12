package com.mrurespect.employeeapp.security;

import com.mrurespect.employeeapp.entity.Employee;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class WebUser {
    @NotNull(message = "username is required")
    private String username;
    @NotNull(message = "role is required")
    private String role;
    @NotNull(message = "password is required")
    @Size(min = 1, message = "is required")
    private String password;
    @NotNull(message = "firstName is required")
    private String firstName;
    @NotNull(message = "lastName is required")
    private String lastName;
    @NotNull(message = "email is required")
    private String email;

    private Employee employee;


    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "WebUser{" +
                "userName='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }


}
