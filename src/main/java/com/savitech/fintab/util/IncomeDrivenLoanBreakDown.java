package com.savitech.fintab.util;

import lombok.Data;

@Data
public class IncomeDrivenLoanBreakDown {
    private String paymentDate;
    private double repaymentAmount;
    private double principalAmount;
    private double interestAmount;
}
