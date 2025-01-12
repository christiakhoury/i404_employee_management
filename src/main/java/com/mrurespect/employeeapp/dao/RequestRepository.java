package com.mrurespect.employeeapp.dao;

import com.mrurespect.employeeapp.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository  extends JpaRepository<Request, Integer> {

}
