package com.savitech.fintab.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.savitech.fintab.entity.CustomerLoan;

public interface CustomerLoanRepository extends JpaRepository <CustomerLoan, String>{
    
    List<CustomerLoan> findAllCustomerLoanByCustomerId(String id, Pageable pageable);

    CustomerLoan findCustomerLoanById(String Id);
}
