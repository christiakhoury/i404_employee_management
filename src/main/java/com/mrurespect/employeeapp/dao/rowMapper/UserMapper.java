//package com.mrurespect.employeeapp.rowMapper;
//
//import com.mrurespect.employeeapp.entity.Role;
//import com.mrurespect.employeeapp.entity.User;
//import org.springframework.jdbc.core.RowMapper;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.Collection;
//
//public class UserMapper implements RowMapper<User> {
//
//    @Override
//    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
//
//        User user = new User();
//        user.setId(rs.getLong("id"));
//        user.setEmail(rs.getString("email"));
//        user.setFirstName(rs.getString("first_name"));
//        user.setLastName(rs.getString("last_name"));
//        user.setPassword(rs.getString("password"));
//        user.setUserName(rs.getString("username"));
////        user.setRoles(rs.getString("role"));
//
//        return user;
//    }
//}
