package com.mrurespect.employeeapp.dao;

import com.mrurespect.employeeapp.entity.Employee;
import com.mrurespect.employeeapp.dao.rowMapper.EmployeeMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class EmployeeDAOimpl implements EmployeeDAO {
    private final EmployeeMapper employeeMapper = new EmployeeMapper();
    private String sql;
    private final JdbcTemplate jdbcTemplate;

    public EmployeeDAOimpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Employee> findAll() {

        sql = "select * from employees";
        return jdbcTemplate.query(sql, employeeMapper);
    }

    @Override
    public Employee findById(int id) {

        sql = "select * from employees where id = ?";
        return jdbcTemplate.queryForObject(sql, employeeMapper, id);
    }

    @Override
    public int save(Employee employee) {

        sql = "insert into employees(email, first_name, last_name) values (?,?,?)";
        return jdbcTemplate.update(sql, employee.getEmail(), employee.getFirstName(), employee.getLastName());
    }

    @Override
    public int deleteByID(int id) {

        sql = "delete from employees where id = ?";
        return jdbcTemplate.update(sql, id);

    }
}