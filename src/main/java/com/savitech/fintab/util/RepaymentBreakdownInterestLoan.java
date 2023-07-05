package com.savitech.fintab.util;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class RepaymentBreakdownInterestLoan {
    private String repaymentDate;
    private BigDecimal monthlyRepayment;
    private BigDecimal interestPayment;
    private BigDecimal principalPayment;
    private BigDecimal remainingLoanAmount;
}
