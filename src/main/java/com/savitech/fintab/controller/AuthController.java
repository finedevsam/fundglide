package com.savitech.fintab.controller;

import com.savitech.fintab.entity.impl.CustomerReg;
import com.savitech.fintab.entity.impl.Login;
import com.savitech.fintab.entity.impl.UpdateProfile;
import com.savitech.fintab.service.impl.LoginServiceImpl;
import com.savitech.fintab.service.impl.ProfileServiceImpl;
import com.savitech.fintab.service.impl.RegisterServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth/")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RegisterServiceImpl registerService;

    @Autowired
    private LoginServiceImpl loginService;

    @Autowired
    private ProfileServiceImpl profileService;


    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody CustomerReg reg){
        return registerService.register(reg);
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody Login login){
        return loginService.signIn(login);
    }

    @PutMapping("profile")
    public ResponseEntity<?> updateProfile(UpdateProfile profile){
        return profileService.updateProfile(profile);
    }

    @GetMapping("me")
    public ResponseEntity<?> loggedInUser(){
        return profileService.loggedInUser();
    }

}
