package com.mrurespect.employeeapp.service;

import com.mrurespect.employeeapp.entity.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<String> findAllNameDepartments() {
        String sql = "SELECT name FROM departments";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("name"));
    }

}
