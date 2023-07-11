package com.savitech.fintab.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.savitech.fintab.entity.LoanConfig;

public interface LoanConfigRepository extends JpaRepository<LoanConfig, String>{
    LoanConfig findLoanConfigByType(String type);

    boolean existsByType(String type);
}
