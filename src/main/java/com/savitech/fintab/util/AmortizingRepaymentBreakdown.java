package com.savitech.fintab.util;

import lombok.Data;

@Data
public class AmortizingRepaymentBreakdown {
    private String paymentDate;
    private double repaymentAmount;
    private double principalAmount;
    private double interestAmount;
}
