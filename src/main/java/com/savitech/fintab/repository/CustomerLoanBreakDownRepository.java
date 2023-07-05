package com.savitech.fintab.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.savitech.fintab.entity.CustomerLoanBreakDown;

public interface CustomerLoanBreakDownRepository extends JpaRepository <CustomerLoanBreakDown, String>{
    
}
