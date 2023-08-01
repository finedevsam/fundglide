package com.savitech.fintab.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.savitech.fintab.dto.TargetSavingsDto;
import com.savitech.fintab.impl.TargetSavingsImpl;

@RestController
@RequestMapping("target-savings")
public class TargetSavingsController {
    
    @Autowired
    private TargetSavingsImpl targetSavingsImpl;

    @PostMapping()
    public ResponseEntity<?> createTargetSavings(@RequestBody TargetSavingsDto targetSavingsDto){
        return targetSavingsImpl.createTargetSavings(targetSavingsDto);
    }

    @GetMapping()
    public ResponseEntity<?> customerTargetSavings(){
        return targetSavingsImpl.customerTargetSavings();
    }
}
