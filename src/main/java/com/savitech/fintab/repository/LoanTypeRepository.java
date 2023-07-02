package com.savitech.fintab.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.savitech.fintab.entity.LoanType;


public interface LoanTypeRepository extends JpaRepository<LoanType, String>{
    LoanType findLoanTypeById(String id);

    boolean existsById(String Id);
}
