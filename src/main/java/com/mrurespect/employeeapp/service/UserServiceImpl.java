package com.mrurespect.employeeapp.service;

import com.mrurespect.employeeapp.dao.EmployeeUserDTOImpl;
import com.mrurespect.employeeapp.dao.RoleDao;
import com.mrurespect.employeeapp.dao.UserDao;
import com.mrurespect.employeeapp.entity.Employee;
import com.mrurespect.employeeapp.entity.Role;
import com.mrurespect.employeeapp.entity.User;
import com.mrurespect.employeeapp.security.WebUser;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    private final RoleDao roleDao;
    private final PasswordEncoder passwordEncoder;
    private EntityManager entityManager;

    @Override
    public User findByUserName1(String theUserName) {

        // retrieve/read from database using username
        TypedQuery<User> theQuery = entityManager.createQuery("from User where userName=:uName", User.class);
        theQuery.setParameter("uName", theUserName);

        User theUser = null;
        try {
            theUser = theQuery.getSingleResult();
        } catch (Exception e) {
            theUser = null;
        }
        return theUser;
    }


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

    @Override
    public void postAddEmployeeRole(Model model, Authentication authentication, EmployeeUserDTOImpl employeeUserDTO) {
        model.addAttribute(new Employee());
        List<String> roles;
        if (authentication.getAuthorities().stream().anyMatch(auth -> "ROLE_ADMIN".equals(auth.getAuthority()))){
            roles = Arrays.asList("ADMIN", "MANAGER", "EMPLOYEE");
        }
        else {
            roles = Arrays.asList("MANAGER", "EMPLOYEE");
        }
        model.addAttribute("roles", roles);
    }

    @Override
    public void postAddUser(User usrname, Employee savedEmployee, EmployeeUserDTOImpl employeeUserDTO) {
        if (Objects.equals(null, usrname)) {
            // Create new user
            WebUser webuser = new WebUser();
            webuser.setUsername(employeeUserDTO.getUsername());
            webuser.setPassword(employeeUserDTO.getPassword());
            webuser.setRole(employeeUserDTO.getRole());
            webuser.setEmployee(savedEmployee);
            save(webuser);
        }
    }
}
