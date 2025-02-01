package com.savitech.fintab.util.loan;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ReducingBalanceLoanBreakDown {
    private String paymentDate;
    private double repaymentAmount;
    private BigDecimal repaymentInterest;
    private BigDecimal principalPayment;
}
