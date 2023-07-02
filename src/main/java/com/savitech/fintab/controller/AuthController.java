package com.savitech.fintab.controller;

import com.savitech.fintab.entity.impl.*;
import com.savitech.fintab.service.impl.AdminLoginServiceImpl;
import com.savitech.fintab.service.impl.LoginServiceImpl;
import com.savitech.fintab.service.impl.ProfileServiceImpl;
import com.savitech.fintab.service.impl.RegisterServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth/")
public class AuthController {

    @Autowired
    private RegisterServiceImpl registerService;

    @Autowired
    private LoginServiceImpl loginService;

    @Autowired
    private ProfileServiceImpl profileService;

    @Autowired
    private AdminLoginServiceImpl adminLoginServiceImpl;


    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody CustomerReg reg){
        return registerService.register(reg);
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody Login login){
        return loginService.signIn(login);
    }


    @PostMapping("admin/login")
    public ResponseEntity<?> adminLogin(@RequestBody AdminLoginModel adminLoginModel){
        return adminLoginServiceImpl.login(adminLoginModel);
    }


    @GetMapping("admin/isme")
    public ResponseEntity<?> adminLoggedInUser(){
        return adminLoginServiceImpl.loggedInAdmin();
    }

    @PutMapping("profile")
    public ResponseEntity<?> updateProfile(UpdateProfile profile){
        return profileService.updateProfile(profile);
    }

    @GetMapping("me")
    public ResponseEntity<?> loggedInUser(){
        return profileService.loggedInUser();
    }

    @PostMapping("reset/password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPassword resetPassword){
        return loginService.resetPassword(resetPassword);
    }

    @PostMapping("reset/confirm")
    public ResponseEntity<?> resetPasswordConfirm(@RequestBody ResetPasswordConfirm passwordConfirm){
        return loginService.confirmPasswordReset(passwordConfirm);
    }

    @PostMapping("change_password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePassword changePassword){
        return profileService.changePassword(changePassword);
    }

    @GetMapping("activate/qr")
    public ResponseEntity<?> activateQR(){
        return profileService.activateQRCodePayment();
    }

}
