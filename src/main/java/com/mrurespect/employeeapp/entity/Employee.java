package com.mrurespect.employeeapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ISEQ$$_83138")
    @SequenceGenerator(name = "ISEQ$$_83138", sequenceName = "ISEQ$$_83138", allocationSize = 1)
    private int id ;

    @Column(name = "first_name")
    private String firstName ;
    @Column(name = "last_name")
    private String lastName;

    private String department;

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @ManyToOne  // Many-to-one relationship with Department
    @JoinColumn(name = "department_id") // Foreign key column to reference Department
    private Department department_id;

    public Department getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(Department department_id) {
        this.department_id = department_id;
    }

    @Column(name = "email")
    private String email;

    @OneToOne(mappedBy = "employee") // Indicates this is the inverse side of the relationship
    private User user_id;


    public User getUser_id() {
        return user_id;
    }

    public void setUser_id(User user_id) {
        this.user_id = user_id;
    }

    public Employee() {
    }
    public Employee(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", department='" + department + '\'' +
                ", department_id=" + department_id +
                ", email='" + email + '\'' +
                ", user_id=" + user_id +
                '}';
    }
}
