package com.savitech.fintab.service;

import org.springframework.http.ResponseEntity;

import com.savitech.fintab.entity.impl.AdminLoginModel;

public interface AdminLoginService {
    ResponseEntity<?> login(AdminLoginModel adminLoginModel);
}
