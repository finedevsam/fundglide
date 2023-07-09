package com.savitech.fintab.util.loan;

import lombok.Data;

@Data
public class DefferedLoanBreakDown {
    private String paymentDate;
    private double repaymentAmount;
    private double principalAmount;
    private double interestAmount;
}
