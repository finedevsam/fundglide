package com.savitech.fintab.service;

import org.springframework.http.ResponseEntity;

import com.savitech.fintab.dto.AdminLoginDto;

public interface AdminLoginService {
    ResponseEntity<?> login(AdminLoginDto adminLoginModel);

    ResponseEntity<?> loggedInAdmin();
}
