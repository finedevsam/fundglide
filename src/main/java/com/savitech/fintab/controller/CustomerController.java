package com.savitech.fintab.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.savitech.fintab.service.impl.CustomerManagerServiceImpl;

@RestController
@RequestMapping("admin/customer")
public class CustomerController {
    @Autowired
    private CustomerManagerServiceImpl customerManagerServiceImpl;

    @GetMapping()
    public ResponseEntity<?> allCustomer(Pageable pageable){
        return customerManagerServiceImpl.allCustomer(pageable);
    }
}
