package com.mrurespect.employeeapp.dao;

import com.mrurespect.employeeapp.entity.Department;
import com.mrurespect.employeeapp.entity.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

@Repository
public class DepartmentDAOImpl implements DepartmentDAO {
    private final EntityManager entityManager;

    public DepartmentDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Department findDepartmentByName(String DepartmentName) {
            TypedQuery<Department> theQuery = entityManager.createQuery("from Department where name=:dep_name", Department.class);
            theQuery.setParameter("dep_name", DepartmentName);

        Department dep = null;
            try {
                dep = theQuery.getSingleResult();
            } catch (Exception e) {
                dep = null;
            }
            return dep;
    }
}

