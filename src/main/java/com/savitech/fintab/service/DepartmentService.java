package com.savitech.fintab.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.savitech.fintab.entity.Department;
import com.savitech.fintab.entity.impl.CreateDepartmentModel;

public interface DepartmentService {
    ResponseEntity<?> createDepartment(CreateDepartmentModel departmentModel);

    Page<Department> allDepartment(Pageable pageable);

    ResponseEntity<?> updateDepartment(String Id, CreateDepartmentModel departmentModel);

    ResponseEntity<?> deleteDepartment(String Id);
}
