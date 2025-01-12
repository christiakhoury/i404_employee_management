package com.mrurespect.employeeapp.dao;


import com.mrurespect.employeeapp.entity.Role;

public interface RoleDao {

    Role findRoleByName(String theRoleName);
}
