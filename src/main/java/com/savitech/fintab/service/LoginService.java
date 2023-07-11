package com.savitech.fintab.service;

import org.springframework.http.ResponseEntity;

import com.savitech.fintab.dto.LoginDto;
import com.savitech.fintab.dto.ResetPasswordDto;
import com.savitech.fintab.dto.ResetPasswordConfirm;

public interface LoginService {
    ResponseEntity<?> signIn(LoginDto login);

    ResponseEntity<?> resetPassword(ResetPasswordDto password);

    ResponseEntity<?> confirmPasswordReset(ResetPasswordConfirm passwordConfirm);

}
