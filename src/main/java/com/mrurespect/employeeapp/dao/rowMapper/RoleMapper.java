//package com.mrurespect.employeeapp.rowMapper;
//
//import com.mrurespect.employeeapp.entity.Role;
//import org.springframework.jdbc.core.RowMapper;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//public class RoleMapper implements RowMapper<Role> {
//
//    @Override
//    public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
//
//        Role role = new Role();
//        role.setId(rs.getLong("id"));
//        role.setName(rs.getString("name"));
//
//        return role;
//    }
//}
