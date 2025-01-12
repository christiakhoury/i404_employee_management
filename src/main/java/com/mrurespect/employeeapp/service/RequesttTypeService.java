package com.mrurespect.employeeapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequesttTypeService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<String> findAllNameRequestType() {
        String sql = "SELECT name FROM request_type";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("name"));
    }
}
