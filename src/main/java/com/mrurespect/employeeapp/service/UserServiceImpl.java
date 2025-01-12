package com.mrurespect.employeeapp.service;

import com.mrurespect.employeeapp.dao.RoleDao;
import com.mrurespect.employeeapp.dao.UserDao;
import com.mrurespect.employeeapp.entity.Role;
import com.mrurespect.employeeapp.entity.User;
import com.mrurespect.employeeapp.security.WebUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    private final RoleDao roleDao;
    private final PasswordEncoder passwordEncoder;


    public UserServiceImpl(UserDao userDao, RoleDao roleDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User findByUserName(String userName) {
        return userDao.findByUserName(userName);
    }

    @Override
    @Transactional
    public User save(WebUser webUser) {
        User user = new User();

        user.setUserName(webUser.getUsername());
        user.setPassword(passwordEncoder.encode(webUser.getPassword()));
        user.setRole(webUser.getRole());
        user.setEmployee(webUser.getEmployee());

        if (webUser.getRole().equals("ADMIN")) {
            user.setRoles(Arrays.asList(roleDao.findRoleByName("ROLE_ADMIN")));
        }
        if (webUser.getRole().equals("MANAGER")) {
            user.setRoles(Arrays.asList(roleDao.findRoleByName("ROLE_MANAGER")));
        }
        if (webUser.getRole().equals("EMPLOYEE")) {
            user.setRoles(Arrays.asList(roleDao.findRoleByName("ROLE_EMPLOYEE")));
        }
        userDao.save(user);
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User uuser = userDao.findByUserName(userName);
        if (uuser == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(uuser.getUserName(), uuser.getPassword(),
                mapRolesToAuthorities(uuser.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    @Transactional
    public void createAdminUserIfNotExists() {
        User usrname = userDao.findByUserName("admin");
        if (Objects.equals(null, usrname)) {
            // Create new admin user
            WebUser webuser = new WebUser();
            webuser.setUsername("admin");
            webuser.setPassword("admin"); // Encode password
            webuser.setRole("ADMIN"); // Assign role to admin
            webuser.setEmail("admin@admin.com");
            webuser.setFirstName("admin");
            webuser.setLastName("admin");
            this.save(webuser); // Save the admin user to the database
            System.out.println("Admin user created with username: admin and password: admin");
        }
    }
}
