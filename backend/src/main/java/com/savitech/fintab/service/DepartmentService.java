package com.savitech.fintab.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.savitech.fintab.dto.CreateDepartmentDto;
import com.savitech.fintab.entity.Department;

public interface DepartmentService {
    ResponseEntity<?> createDepartment(CreateDepartmentDto departmentModel);

    Page<Department> allDepartment(Pageable pageable);

    ResponseEntity<?> updateDepartment(String Id, CreateDepartmentDto departmentModel);

    ResponseEntity<?> deleteDepartment(String Id);
}
