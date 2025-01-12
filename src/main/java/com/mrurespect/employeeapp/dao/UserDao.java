package com.mrurespect.employeeapp.dao;


import com.mrurespect.employeeapp.entity.User;


public interface UserDao {

    User findByUserName(String userName);

    User save(User uuser);

}
