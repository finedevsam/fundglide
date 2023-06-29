package com.savitech.fintab.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.savitech.fintab.entity.Permission;

public interface PermissionRepository extends JpaRepository<Permission, String>{
    Boolean existsByName(String name);
    Boolean existsByRole(String role);
    boolean existsById(String Id);
    Permission findPermissionById(String id);
}
