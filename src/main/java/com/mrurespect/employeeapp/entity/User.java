package com.mrurespect.employeeapp.entity;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "uuser")
public class User {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ISEQ$$_83145")
    @SequenceGenerator(name = "ISEQ$$_83145", sequenceName = "ISEQ$$_83145", allocationSize = 1)
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String userName;

    private String role;

    private String password;

//    @Column(name = "first_name")
//    private String firstName;
//
//    @Column(name = "last_name")
//    private String lastName;
//
//    private String email;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role> roles;

    @OneToOne
    @JoinColumn(name = "employee_id") // This column in the User table will store the Employee ID
    private Employee employee;

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public User() {
    }

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public User(String userName, String password, Collection<Role> roles) {
        this.userName = userName;
        this.password = password;
        this.roles = roles;
    }

    public User(String userName, String password, Employee employee, Collection<Role> roles) {
        this.userName = userName;
        this.password = password;
        this.employee = employee;
        this.roles = roles;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                '}';
    }
}