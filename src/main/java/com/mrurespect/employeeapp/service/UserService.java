package com.mrurespect.employeeapp.service;

import com.mrurespect.employeeapp.dao.EmployeeUserDTOImpl;
import com.mrurespect.employeeapp.entity.Employee;
import com.mrurespect.employeeapp.entity.User;
import com.mrurespect.employeeapp.security.WebUser;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.ui.Model;

public interface UserService extends UserDetailsService {

    User findByUserName(String userName);

    @Transactional
    User save(WebUser webUser);

    void createAdminUserIfNotExists();

    void postAddEmployeeRole(Model model, Authentication authentication, EmployeeUserDTOImpl employeeUserDTO);

    void postAddUser(User usrname, Employee savedEmployee, EmployeeUserDTOImpl employeeUserDTO);
}
