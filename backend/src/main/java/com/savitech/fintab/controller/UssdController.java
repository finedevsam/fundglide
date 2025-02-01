package com.savitech.fintab.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.savitech.fintab.impl.UssdServiceImpl;

@RestController
@RequestMapping("/ussd")
public class UssdController {

    @Autowired
    private UssdServiceImpl ussdServiceImpl;

    @PostMapping()
    public String implementsUssd(@RequestBody String requestBody) {
        return ussdServiceImpl.implementUssd(requestBody);
    }
}
