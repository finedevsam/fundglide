package com.savitech.fintab.util.loan;

import lombok.Data;

@Data
public class InterestOnlyLoanBreakDown {
    private String paymentDate;
    private double repaymentAmount;
    private double principalAmount;
    private double interestAmount;
}
