package com.mrurespect.employeeapp.dao;


import com.mrurespect.employeeapp.entity.Role;
import com.mrurespect.employeeapp.entity.User;

public interface RoleDao {

    Role findRoleByName(String theRoleName);

}
