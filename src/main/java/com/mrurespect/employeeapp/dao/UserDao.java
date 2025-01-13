package com.mrurespect.employeeapp.dao;


import com.mrurespect.employeeapp.entity.User;
import jakarta.transaction.Transactional;


public interface UserDao {

    User findByUserName(String userName);

    @Transactional
    User save(User uuser);

}
