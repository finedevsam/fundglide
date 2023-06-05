package com.savitech.fintab.service;

import com.savitech.fintab.entity.impl.Login;
import com.savitech.fintab.entity.impl.ResetPassword;
import com.savitech.fintab.entity.impl.ResetPasswordConfirm;
import org.springframework.http.ResponseEntity;

public interface LoginService {
    ResponseEntity<?> signIn(Login login);

    ResponseEntity<?> resetPassword(ResetPassword password);

    ResponseEntity<?> confirmPasswordReset(ResetPasswordConfirm passwordConfirm);

}
