package com.savitech.fintab.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.savitech.fintab.entity.CustomerLoanBreakDown;

public interface CustomerLoanBreakDownRepository extends JpaRepository <CustomerLoanBreakDown, String>{

    List<CustomerLoanBreakDown> findAllCustomerLoanBreakDownsByCustomerLoanIdAndCustomerId(String loanId, String customerId);
    List<CustomerLoanBreakDown> findAllCustomerLoanBreakDownsByCustomerLoanId(String Id);
    
}
