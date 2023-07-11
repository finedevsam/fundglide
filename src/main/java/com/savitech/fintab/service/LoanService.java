package com.savitech.fintab.service;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.savitech.fintab.dto.ApplyForLoanDto;
import com.savitech.fintab.dto.ApproveLoanDto;
import com.savitech.fintab.dto.LoanConfigDto;
import com.savitech.fintab.dto.LoanTypeDto;

public interface LoanService {
    ResponseEntity<?> createLoanType(LoanTypeDto loanTypeModel);

    ResponseEntity<?> allLoan(Pageable pageable);

    ResponseEntity<?> updateLoanType(String Id, LoanTypeDto loanTypeModel);

    ResponseEntity<?> applyForLoan(ApplyForLoanDto loan);

    ResponseEntity<?> myLoans(Pageable pageable);

    ResponseEntity<?> viewLoanBreakdown(String loanId);

    ResponseEntity<?> adminAllCustomerLoan(Pageable pageable);

    ResponseEntity<?> adminViewLoanBreakdown(String loanId);

    ResponseEntity<?> approveLoan(String loanId, ApproveLoanDto approveLoanDto);

    ResponseEntity<?> configureLoan(LoanConfigDto loanConfigDto);

    ResponseEntity<?> loanConfiguration();
}
