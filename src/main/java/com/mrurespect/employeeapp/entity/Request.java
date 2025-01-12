package com.mrurespect.employeeapp.entity;


import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "requests")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    private String status;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;  // Link to employee one employee can have many requests

    @ManyToOne
    @JoinColumn(name = "request_type_id")
    private RequesttType  request_type_id;// Link to request type one request type can be associated to many requests

    @ManyToOne
    @JoinColumn(name = "state", nullable = false)
    private RequestState state;

    @Column(name = "request_date") // Map to the column in the DB
    private LocalDate request_date; // Use LocalDate for the date field

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public RequesttType getRequest_type_id() {
        return request_type_id;
    }

    public void setRequest_type_id(RequesttType request_type_id) {
        this.request_type_id = request_type_id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public RequestState getState() {
        return state;
    }

    public void setState(RequestState state) {
        this.state = state;
    }

    public LocalDate getRequest_date() {
        return request_date;
    }

    public void setRequest_date(LocalDate request_date) {
        this.request_date = request_date;
    }

//    public String getDepartment() {
//        return employee.getDepartment();
//    }
}