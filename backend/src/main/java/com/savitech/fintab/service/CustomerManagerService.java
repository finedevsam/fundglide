package com.savitech.fintab.service;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface CustomerManagerService {
    
    ResponseEntity<?> allCustomer(Pageable pageable);

    ResponseEntity<?> viewCustomerAccount(String customerId);

    ResponseEntity<?> allCustomerTransactions(Pageable pageable);

    ResponseEntity<?> viewAccountTransactions(String accountNo, Pageable pageable);
}
