package com.savitech.fintab.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;

@RestController
public class Welcome {

    @GetMapping("/")
    public ResponseEntity<?> WelcomePaage() {
        Map<Object, String> data = new HashMap<>();
        data.put("Version", "V1");
        data.put("message", "Welcome to Fundglide");
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

}
