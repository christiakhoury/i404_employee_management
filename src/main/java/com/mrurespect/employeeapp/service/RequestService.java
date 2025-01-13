package com.mrurespect.employeeapp.service;

import com.mrurespect.employeeapp.entity.Department;
import com.mrurespect.employeeapp.entity.Employee;
import com.mrurespect.employeeapp.entity.Request;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RequestService {
    List<Request> getRequestsByDepartment(Department departmentId);

    Page<Request> getPaginatedRequests(int page, int size, Department departmentId);

    Page<Request> getHisRequests(int page, int size, Employee employee_id);

    void save(Request requestId);
}
