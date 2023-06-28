package com.savitech.fintab.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.savitech.fintab.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, String>{
    
}
