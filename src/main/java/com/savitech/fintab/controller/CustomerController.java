package com.savitech.fintab.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.savitech.fintab.impl.CustomerManagerServiceImpl;

@RestController
@RequestMapping("admin/customer")
public class CustomerController {
    @Autowired
    private CustomerManagerServiceImpl customerManagerServiceImpl;

    @GetMapping()
    public ResponseEntity<?> allCustomer(Pageable pageable){
        return customerManagerServiceImpl.allCustomer(pageable);
    }

    @GetMapping("/{id}/account")
    public ResponseEntity<?> viewCustomerAccounts(@PathVariable String id){
        return customerManagerServiceImpl.viewCustomerAccount(id);
    }

    @GetMapping("/transactions")
    public ResponseEntity<?> allTransactionLogs(Pageable pageable){
        return customerManagerServiceImpl.allCustomerTransactions(pageable);
    }

    @GetMapping("/account/logs")
    public ResponseEntity<?> viewAccountTransactions(@RequestParam(value = "accountNo") String accountNo, Pageable pageable){
        return customerManagerServiceImpl.viewAccountTransactions(accountNo, pageable);
    }
}
