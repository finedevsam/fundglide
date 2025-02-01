package com.savitech.fintab.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.savitech.fintab.entity.AdminUser;

public interface AdminUserRepository extends JpaRepository<AdminUser, String>{

    AdminUser findByUserId(String id);
    AdminUser findAUserById(String id);
    
}
