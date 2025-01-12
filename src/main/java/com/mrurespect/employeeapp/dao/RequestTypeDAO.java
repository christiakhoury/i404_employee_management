package com.mrurespect.employeeapp.dao;

import com.mrurespect.employeeapp.entity.RequesttType;

import java.util.List;

public interface RequestTypeDAO {
    RequesttType findRequestTypeByName(String RequestTypeName);
    void save(RequesttType requestType);
}
