package com.savitech.fintab.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.savitech.fintab.dto.LoanConfigDto;
import com.savitech.fintab.impl.LoanServiceImpl;

@RestController
@RequestMapping("config")
public class SystemConfigController {
    
    @Autowired
    private LoanServiceImpl loanServiceImpl;

    @GetMapping("/{service}")
    public ResponseEntity<?> systemConfiguration(@PathVariable String service){
        if(Objects.equals(service, "loan")){
            return loanServiceImpl.loanConfiguration();
        }else{
            Map<Object, Object> data = new HashMap<>();
            data.put("message", "Invalid Parameter");
            data.put("code", 400);
            data.put("status", "fail");
            return ResponseEntity.badRequest().body(data);
        }
    }

    @PostMapping("/loan")
    public ResponseEntity<?> configureLoan(@RequestBody LoanConfigDto loanConfigDto){
        return loanServiceImpl.configureLoan(loanConfigDto);
    }
}
