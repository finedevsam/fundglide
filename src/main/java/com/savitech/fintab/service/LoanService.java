package com.savitech.fintab.service;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.savitech.fintab.entity.impl.ApplyForLoan;
import com.savitech.fintab.entity.impl.LoanTypeModel;

public interface LoanService {
    ResponseEntity<?> createLoanType(LoanTypeModel loanTypeModel);

    ResponseEntity<?> allLoan(Pageable pageable);

    ResponseEntity<?> updateLoanType(String Id, LoanTypeModel loanTypeModel);

    ResponseEntity<?> applyForLoan(ApplyForLoan loan);

    ResponseEntity<?> myLoans(Pageable pageable);
}
