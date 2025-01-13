package com.mrurespect.employeeapp.service;

import com.mrurespect.employeeapp.dao.*;
import com.mrurespect.employeeapp.entity.Department;
import com.mrurespect.employeeapp.entity.Employee;
import com.mrurespect.employeeapp.entity.Request;
import com.mrurespect.employeeapp.entity.RequesttType;
import com.mrurespect.employeeapp.entity.dto.RequestDTO;
import org.springframework.data.domain.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Objects;

@Service
public class RequestServiceImpl implements RequestService{

    private final JdbcTemplate jdbcTemplate;
    private final RequestTypeDAO requestTypeDAO;
    private final RequestRepository requestRepository;
    private final RequestStateDAO requestStateDAO;
    private final EmployeeDAO employeeDAO;
    private final UserDao userDao;

    public RequestServiceImpl(JdbcTemplate jdbcTemplate, RequestTypeDAO requestTypeDAO, RequestRepository requestRepository, RequestStateDAO requestStateDAO, EmployeeDAO employeeDAO, UserDao userDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.requestTypeDAO = requestTypeDAO;
        this.requestRepository = requestRepository;
        this.requestStateDAO = requestStateDAO;
        this.employeeDAO = employeeDAO;
        this.userDao = userDao;
    }

    private RequesttType findRequestTypeById(Long requestTypeId) {
        String sql = "SELECT * FROM request_type WHERE id = ?";
        return jdbcTemplate.queryForObject(
                sql,
                new Object[]{requestTypeId},
                (rs, rowNum) -> {
                    RequesttType requestType = new RequesttType();
                    requestType.setId(rs.getLong("id"));
                    requestType.setName(rs.getString("name"));
                    return requestType;
                });
    }

    private Request findRequestById(Long Id) {
        String sql = "SELECT * FROM requests WHERE id = ?";
        return jdbcTemplate.queryForObject(
                sql,
                new Object[]{Id},
                (rs, rowNum) -> {
                    Request request = new Request();
                    request.setId(rs.getLong("id"));
                    request.setRequest_date(rs.getDate("request_date").toLocalDate());
                    request.setRequest_type(rs.getString("request_type"));
                    request.setStatus(rs.getString("status"));
                    Employee employee_rec = employeeDAO.findById(rs.getInt("employee_id"));
                    request.setEmployee(employee_rec);
                    request.setName(rs.getString("name"));
                    return request;
                });
    }


    @Override
    public List<Request> getRequestsByDepartment(Department departmentId) {
        String sql = "SELECT r.*, rt.id AS request_type_id, rt.name AS request_type_name " +
                "FROM employees e " +
                "JOIN requests r ON e.id = r.employee_id " +
                "JOIN request_type rt ON r.request_type_id = rt.id " +
                "WHERE e.department_id = ?";

        return jdbcTemplate.query(
                sql,
                new Object[]{departmentId.getId()},
                (rs, rowNum) -> {
                    // Create and map Request object
                    Request request = new Request();
                    request.setId(rs.getLong("id"));
                    request.setRequest_date(rs.getDate("request_date").toLocalDate()); // Adjust if needed

                    Long requestTypeId = rs.getLong("request_type_id");
                    RequesttType requestType = findRequestTypeById(requestTypeId);
                    request.setRequest_type_id(requestType);
                    request.setStatus(rs.getString("status"));
                    request.setName(rs.getString("name"));
                    Employee employee_rec = employeeDAO.findById(rs.getInt("employee_id"));
                    request.setEmployee(employee_rec);
                    request.setRequest_type(rs.getString("request_type_name"));

                    return request;
                }
        );
    }

    @Override
    public Page<Request> getPaginatedRequests(int page, int size, Department departmentId) {
        // Calculate the offset
        int offset = page * size;

        // SQL query with LIMIT and OFFSET for pagination
        String sql = "SELECT r.*, rt.id AS request_type_id, rt.name AS request_type_name " +
                "FROM employees e " +
                "JOIN requests r ON e.id = r.employee_id " +
                "JOIN request_type rt ON r.request_type_id = rt.id " +
                "WHERE e.department_id = ? " +
                "LIMIT ? OFFSET ?";

        // Fetch paginated results
        List<Request> requests = jdbcTemplate.query(
                sql,
                new Object[]{departmentId.getId(), size, offset},
                (rs, rowNum) -> {
                    Request request = new Request();
                    request.setId(rs.getLong("id"));
                    request.setRequest_date(rs.getDate("request_date").toLocalDate()); // Adjust if needed

                    Long requestTypeId = rs.getLong("request_type_id");
                    RequesttType requestType = findRequestTypeById(requestTypeId);
                    request.setRequest_type_id(requestType);
                    request.setStatus(rs.getString("status"));
                    request.setName(rs.getString("name"));
                    Employee employee_rec = employeeDAO.findById(rs.getInt("employee_id"));
                    request.setEmployee(employee_rec);
                    request.setRequest_type(rs.getString("request_type_name"));

                    return request;
                }
        );

        // To calculate the total number of pages, first get the total count of requests in the department
        String countSql = "SELECT COUNT(*) FROM employees e " +
                "JOIN requests r ON e.id = r.employee_id " +
                "WHERE e.department_id = ?";

        int totalRecords = jdbcTemplate.queryForObject(countSql, new Object[]{departmentId.getId()}, Integer.class);
        int totalPages = (int) Math.ceil((double) totalRecords / size);

        // Create and return a Page object (from Spring Data's Page class)
        return new PageImpl<>(requests, PageRequest.of(page, size), totalRecords);
    }

    @Override
    public Page<Request> getHisRequests(int page, int size, Employee employee_id) {
        // Calculate the offset
        int offset = page * size; // Use zero-based page indexing

        // SQL query to fetch paginated results
        String sql = "SELECT r.*, rt.id AS request_type_id, rt.name AS request_type_name " +
                "FROM employees e " +
                "JOIN requests r ON e.id = r.employee_id " +
                "JOIN request_type rt ON r.request_type_id = rt.id " +
                "WHERE e.id = ? " +
                "LIMIT ? OFFSET ?";

        // Fetch paginated results
        List<Request> requests = jdbcTemplate.query(
                sql,
                new Object[]{employee_id.getId(), size, offset},
                (rs, rowNum) -> {
                    Request request = new Request();
                    request.setId(rs.getLong("id"));
                    request.setRequest_date(rs.getDate("request_date").toLocalDate());

                    Long requestTypeId = rs.getLong("request_type_id");
                    RequesttType requestType = findRequestTypeById(requestTypeId);
                    request.setRequest_type_id(requestType);
                    request.setStatus(rs.getString("status"));
                    request.setName(rs.getString("name"));
                    Employee employee_rec = employeeDAO.findById(rs.getInt("employee_id"));
                    request.setEmployee(employee_rec);
                    request.setRequest_type(rs.getString("request_type_name"));
                    return request;
                }
        );

        // SQL query to count the total number of requests for this employee
        String countSql = "SELECT COUNT(*) " +
                "FROM employees e " +
                "JOIN requests r ON e.id = r.employee_id " +
                "WHERE e.id = ?";

        // Get the total number of records
        int totalRecords = jdbcTemplate.queryForObject(countSql, new Object[]{employee_id.getId()}, Integer.class);

        // Create and return a Page object (from Spring Data's Page class)
        return new PageImpl<>(requests, PageRequest.of(page, size), totalRecords); // No subtraction here
    }

    @Override
    @Transactional
    public void save(Request requestId) {
        Request request;
        if (!Objects.equals(null, requestId)){
            request = requestId;
        }
        else {
            request = new Request();
        }

        request.setEmployee(requestId.getEmployee());
        request.setRequest_date(requestId.getRequest_date());
        request.setRequest_type(requestId.getRequest_type());
        request.setStatus(requestId.getStatus());
        request.setName(requestId.getName());

        if (requestId.getRequest_type().equals("SICK_LEAVE")) {
            request.setRequest_type_id(requestTypeDAO.findRequestTypeByName("SICK_LEAVE"));
        }
        if (requestId.getRequest_type().equals("WORK_FROM_HOME")) {
            request.setRequest_type_id(requestTypeDAO.findRequestTypeByName("WORK_FROM_HOME"));
        }
        if (requestId.getRequest_type().equals("HALF_DATE")) {
            request.setRequest_type_id(requestTypeDAO.findRequestTypeByName("HALF_DATE"));
        }
        if (requestId.getRequest_type().equals("VACATION")) {
            request.setRequest_type_id(requestTypeDAO.findRequestTypeByName("VACATION"));
        }

        if (requestId.getStatus().equals("PENDING")) {
            request.setState(requestStateDAO.findRequestStateByName("PENDING"));
        }
        if (requestId.getStatus().equals("APPROVED")) {
            request.setState(requestStateDAO.findRequestStateByName("APPROVED"));
        }
        if (requestId.getStatus().equals("REJECTED")) {
            request.setState(requestStateDAO.findRequestStateByName("REJECTED"));
        }

        requestRepository.save(request);
    }

    @Override
    public void acceptrejectRequest(Long id, RequestDTO requestDAO, Model model, Authentication authentication, String new_state) {
        Request request_id =  findRequestById(id);
        request_id.setStatus(new_state);
        save(request_id);
    }

    @Override
    public Page<Request> getAllPaginatedRequests(int page, int size) {
        return requestRepository.findAll(PageRequest.of(page, size, Sort.by("id").ascending()));
    }

    @Override
    public Page<Request> getRequestsByStatus(int page, int size, String status) {
        Pageable pageable = PageRequest.of(page, size);
        return requestRepository.findByStatus(status, pageable);
    }

//    @Override
//    public Page<Request> getPaginatedRequestsByDepartmentAndStatus(int page, int size, Department departmentId, String status) {
//            Pageable pageable = PageRequest.of(page, size);
//            return requestRepository.findByDepartmentIdAndStatus(departmentId, status, pageable);
//        }

    @Override
    public Page<Request> getHisRequestsByStatus(int page, int size, Employee employee, String status) {
            Pageable pageable = PageRequest.of(page, size);
            return requestRepository.findByEmployeeAndStatus(employee, status, pageable);
    }

}