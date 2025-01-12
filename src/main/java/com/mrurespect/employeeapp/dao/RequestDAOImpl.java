package com.mrurespect.employeeapp.dao;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class RequestDAOImpl implements RequestDto{

    private String name;
    private String status;

    @JsonFormat(pattern = "mm/dd/yyyy")
    private LocalDate request_date;

    private String request_type;

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

    public LocalDate getRequest_date() {
        return request_date;
    }

    public void setRequest_date(LocalDate request_date) {
        this.request_date = request_date;
    }

    public String getRequest_type() {
        return request_type;
    }

    public void setRequest_type(String request_type) {
        this.request_type = request_type;
    }
}
