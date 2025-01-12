package com.mrurespect.employeeapp.entity;

import com.mrurespect.employeeapp.dao.UserDao;
import com.mrurespect.employeeapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class AdminUserInitializer implements CommandLineRunner {

    @Autowired
    private UserDao userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {
        userService.createAdminUserIfNotExists();
    }

}
