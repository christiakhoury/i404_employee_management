package com.mrurespect.employeeapp.dao.rowMapper;

import com.mrurespect.employeeapp.entity.Employee;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeMapper implements RowMapper<Employee> {

    @Override
    public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {

        Employee employee = new Employee();
        employee.setId(rs.getInt("id"));
        employee.setEmail(rs.getString("email"));
        employee.setFirstName(rs.getString("first_name"));
        employee.setLastName(rs.getString("last_name"));

        return employee;
    }
}
