package com.mrurespect.employeeapp.dao;

import com.mrurespect.employeeapp.entity.RequestState;
import com.mrurespect.employeeapp.entity.RequesttType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

@Repository
public class RequestStateDAO {
     private final EntityManager entityManager;

    public RequestStateDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public RequestState findRequestStateByName(String state) {
        TypedQuery<RequestState> theQuery = entityManager.createQuery("from RequestState where name=:status", RequestState.class);
        theQuery.setParameter("status", state);

        RequestState request_state = null;
        try {
            request_state = theQuery.getSingleResult();
        } catch (Exception e) {
            request_state = null;
        }
        return request_state;
    }

    public void save(RequesttType requestType) {
        entityManager.persist(requestType);
    }
}

