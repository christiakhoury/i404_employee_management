package com.mrurespect.employeeapp.service;

import com.mrurespect.employeeapp.dao.RequestRepository;
import com.mrurespect.employeeapp.dao.RequestStateDAO;
import com.mrurespect.employeeapp.dao.RequestTypeDAO;
import com.mrurespect.employeeapp.entity.Department;
import com.mrurespect.employeeapp.entity.Employee;
import com.mrurespect.employeeapp.entity.Request;
import com.mrurespect.employeeapp.entity.RequesttType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class RequestServiceImpl implements RequestService{

    private final JdbcTemplate jdbcTemplate;
    private final RequestTypeDAO requestTypeDAO;
    private final RequestRepository requestRepository;
    private final RequestStateDAO requestStateDAO;

    public RequestServiceImpl(JdbcTemplate jdbcTemplate, RequestTypeDAO requestTypeDAO, RequestRepository requestRepository, RequestStateDAO requestStateDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.requestTypeDAO = requestTypeDAO;
        this.requestRepository = requestRepository;
        this.requestStateDAO = requestStateDAO;
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

    @Override
    public List<Request> getRequestsByDepartment(Department departmentId) {
        String sql = "SELECT r.*, rt.id AS request_type_id, rt.name AS request_type_name\n" +
                "        FROM employees e\n" +
                "        JOIN requests r ON e.id = r.employee_id\n" +
                "        JOIN request_type rt ON r.request_type_id = rt.id\n" +
                "        WHERE e.department_id = ?";

        return jdbcTemplate.query(
                sql,
                new Object[]{departmentId.getId()},
                (rs, rowNum) -> {
                    // Create and map Request object
                    Request request = new Request();
                    request.setId(rs.getLong("id"));
//                    request.setRequestState(rs.getString("request_state"));
                    request.setRequest_date(rs.getDate("date").toLocalDate()); // Adjust if needed


                    Long request_type = rs.getLong("request_type_id");
                    RequesttType requestType = findRequestTypeById(request_type);
                    request.setRequest_type_id(requestType);

                    return request;
                }
        );
    }

//    @Override
//    public Page<Request> getPaginatedRequests(int page, int size, Department departmentId) {
//        return null;
//    }

    @Override
    public Page<Request> getPaginatedRequests(int page, int size, Department departmentId) {
        if (page < 1) {
            page = 1;  // Set the page to 1 if it's less than 1
        }
        // Calculate the offset
        int offset = (page - 1) * size;

        // SQL query with LIMIT and OFFSET for pagination
        String sql = "SELECT r.*, rt.id AS request_type_id, rt.name AS request_type_name\n" +
                "FROM employees e\n" +
                "JOIN requests r ON e.id = r.employee_id\n" +
                "JOIN request_type rt ON r.request_type_id = rt.id\n" +
                "WHERE e.department_id = ?\n" +
                "LIMIT ? OFFSET ?";

        // Fetch paginated results
        List<Request> requests = jdbcTemplate.query(
                sql,
                new Object[]{departmentId.getId(), size, offset},
                (rs, rowNum) -> {
                    Request request = new Request();
                    request.setId(rs.getLong("id"));
                    request.setRequest_date(rs.getDate("date").toLocalDate()); // Adjust if needed

                    Long requestTypeId = rs.getLong("request_type_id");
                    RequesttType requestType = findRequestTypeById(requestTypeId);
                    request.setRequest_type_id(requestType);

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
        return new PageImpl<>(requests, PageRequest.of(page - 1, size), totalRecords);
    }

    @Override
    public Page<Request> getHisRequests(int page, int size,Employee employee_id) {
        if (page < 1) {
            page = 1;  // Set the page to 1 if it's less than 1
        }
        // Calculate the offset
        int offset = (page - 1) * size;

        String sql = "SELECT r.*, rt.id AS request_type_id, rt.name AS request_type_name " +
                "FROM employees e " +
                "JOIN requests r ON e.id = r.employee_id " +
                "JOIN request_type rt ON r.request_type_id = rt.id " +
                "WHERE e.id = ? " +
                "LIMIT ? OFFSET ?";

        List<Request> requests = jdbcTemplate.query(
                sql,
                new Object[]{employee_id.getId(), size, offset},
                (rs, rowNum) -> {
                    Request request = new Request();
                    request.setId(rs.getLong("id"));
                    request.setRequest_date(rs.getDate("request_date").toLocalDate()); // Adjust if needed

                    Long requestTypeId = rs.getLong("request_type_id");
                    RequesttType requestType = findRequestTypeById(requestTypeId);
                    request.setRequest_type_id(requestType);
                    request.setStatus(rs.getString("status"));
                    request.setName(rs.getString("name"));
//                    request.setEmployee(rs.getObject("employee_id"));
                    return request;
                }
        );



//        // SQL query with LIMIT and OFFSET for pagination
//        String sql = "SELECT r.*, rt.id AS request_type_id, rt.name AS request_type_name FROM employees e JOIN requests r ON e.id = r.employee_id JOIN request_type rt ON r.request_type_id = rt.id WHERE e.id = ? LIMIT ? OFFSET ?";
//
//        // Fetch paginated results
//        List<Request> requests = jdbcTemplate.query(
//                sql,
//                new Object[]{employee_id.getId(), size, offset},
//                (rs, rowNum) -> {
//                    Request request = new Request();
//                    request.setId(rs.getLong("id"));
//                    request.setRequest_date(rs.getDate("date").toLocalDate()); // Adjust if needed
//
//                    Long requestTypeId = rs.getLong("request_type_id");
//                    RequesttType requestType = findRequestTypeById(requestTypeId);
//                    request.setRequest_type_id(requestType);
//
//                    return request;
//                }
//        );

        // To calculate the total number of pages, first get the total count of requests in the department
        String countSql = "SELECT COUNT(*) FROM employees e " +
                "JOIN requests r ON e.id = r.employee_id " +
                "WHERE e.department_id = ?";

        int totalRecords = jdbcTemplate.queryForObject(countSql, new Object[]{employee_id.getId()}, Integer.class);
        int totalPages = (int) Math.ceil((double) totalRecords / size);

        // Create and return a Page object (from Spring Data's Page class)
        return new PageImpl<>(requests, PageRequest.of(page - 1, size), totalRecords);
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

}


