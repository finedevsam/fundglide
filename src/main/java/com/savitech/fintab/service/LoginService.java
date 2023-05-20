package com.savitech.fintab.service;

import com.savitech.fintab.entity.impl.Login;
import org.springframework.http.ResponseEntity;

public interface LoginService {
    ResponseEntity<?> signIn(Login login);
}
