package com.mrurespect.employeeapp.dao;

import com.mrurespect.employeeapp.entity.Employee;
import com.mrurespect.employeeapp.entity.Request;
import com.mrurespect.employeeapp.entity.RequesttType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class RequestTypeDAOImpl implements RequestTypeDAO {
    private final EntityManager entityManager;

    public RequestTypeDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public RequesttType findRequestTypeByName(String RequestTypeName) {
        TypedQuery<RequesttType> theQuery = entityManager.createQuery("from RequesttType where name=:request_name", RequesttType.class);
        theQuery.setParameter("request_name", RequestTypeName);

        RequesttType request_type_rec = null;
        try {
            request_type_rec = theQuery.getSingleResult();
        } catch (Exception e) {
            request_type_rec = null;
        }
        return request_type_rec;
    }

    @Override
    public void save(RequesttType requestType) {
        entityManager.persist(requestType);
    }
}
