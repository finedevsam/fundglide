package com.savitech.fintab.controller;

import com.savitech.fintab.entity.impl.CustomerReg;
import com.savitech.fintab.entity.impl.Login;
import com.savitech.fintab.security.CustomUserDetailsService;
import com.savitech.fintab.service.impl.LoginServiceImpl;
import com.savitech.fintab.service.impl.RegisterServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private CustomUserDetailsService userDetailsService;


    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody CustomerReg reg){
        return registerService.register(reg);
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody Login login){
        return loginService.signIn(login);
    }

}
