package com.mrurespect.employeeapp.dao;

import com.mrurespect.employeeapp.entity.Department;
import com.mrurespect.employeeapp.entity.Role;

public interface DepartmentDAO {

    Department findDepartmentByName(String theRoleName);

}
