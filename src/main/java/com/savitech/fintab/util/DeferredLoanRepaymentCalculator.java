package com.savitech.fintab.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class DeferredLoanRepaymentCalculator {
    // public static void main(String[] args) {
    //     double loanAmount = 10000; // Example loan amount
    //     double annualInterestRate = 5.0; // Example annual interest rate
    //     int loanTermMonths = 12; // Loan term in months
    //     int defermentMonths = 6; // Number of months for deferment

    //     List<RepaymentBreakdown> breakdown = calculateDeferredLoanRepayment(loanAmount, annualInterestRate, loanTermMonths, defermentMonths);

    //     // Print the breakdown
    //     for (RepaymentBreakdown repayment : breakdown) {
    //         System.out.println("Payment Date: " + repayment.getPaymentDate() + ", Repayment Amount: $" + repayment.getRepaymentAmount()
    //                 + ", Principal Amount: $" + repayment.getPrincipalAmount() + ", Interest Amount: $" + repayment.getInterestAmount());
    //     }
    // }

    public List<DefferedLoanBreakDown> calculateDeferredLoanRepayment(double loanAmount, double annualInterestRate, int loanTermMonths, int defermentMonths) {
        List<DefferedLoanBreakDown> breakdown = new ArrayList<>();
        BigDecimal monthlyInterestRate = BigDecimal.valueOf(annualInterestRate).divide(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);
        BigDecimal monthlyPayment = BigDecimal.valueOf(0);
        BigDecimal remainingBalance = BigDecimal.valueOf(loanAmount);
        LocalDate startDate = LocalDate.now();

        for (int i = 1; i <= loanTermMonths; i++) {
            LocalDate paymentDate = startDate.plusMonths(i);
            String formattedDate = paymentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            BigDecimal interestAmount = remainingBalance.multiply(monthlyInterestRate).setScale(2, RoundingMode.HALF_UP);
            BigDecimal principalAmount = (i > defermentMonths) ? monthlyPayment.subtract(interestAmount).setScale(2, RoundingMode.HALF_UP) : BigDecimal.valueOf(0);
            remainingBalance = remainingBalance.subtract(principalAmount).setScale(2, RoundingMode.HALF_UP);
            DefferedLoanBreakDown repayment = new DefferedLoanBreakDown();
            repayment.setInterestAmount(interestAmount.doubleValue());
            repayment.setPaymentDate(formattedDate);
            repayment.setRepaymentAmount(monthlyPayment.doubleValue());
            repayment.setPrincipalAmount(principalAmount.doubleValue());
            breakdown.add(repayment);
        }

        return breakdown;
    }
}