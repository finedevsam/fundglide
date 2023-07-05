package com.savitech.fintab.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LoanRepaymentCalculatorReducingBalance {
    public static void main(String[] args) {
        double loanAmount = 10000; // Example loan amount
        double annualInterestRate = 5.0; // Example annual interest rate
        int loanTermMonths = 12; // Loan term in months

        List<RepaymentBreakdownReducting> breakdown = calculateLoanRepaymentBreakdown(loanAmount, annualInterestRate, loanTermMonths);

        // Print the breakdown
        for (RepaymentBreakdownReducting repayment : breakdown) {
            System.out.println(
                "Payment Date: " + repayment.getPaymentDate() + 
                ", Repayment Amount: $" + repayment.getRepaymentAmount() + 
                ", Interest: $" + repayment.getRepaymentInterest() + 
                ", Principal Amount: $" + repayment.getPrincipalPayment()
                );
        }
    }

    public static List<RepaymentBreakdownReducting> calculateLoanRepaymentBreakdown(double loanAmount, double annualInterestRate, int loanTermMonths) {
        List<RepaymentBreakdownReducting> breakdown = new ArrayList<>();
        BigDecimal monthlyInterestRate = BigDecimal.valueOf(annualInterestRate).divide(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);
        BigDecimal monthlyRepayment = calculateMonthlyRepayment(loanAmount, monthlyInterestRate, loanTermMonths);
        BigDecimal remainingBalance = BigDecimal.valueOf(loanAmount);
        LocalDate startDate = LocalDate.now();

        for (int i = 1; i <= loanTermMonths; i++) {
            LocalDate paymentDate = startDate.plusMonths(i);
            String formattedDate = paymentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            BigDecimal interestAmount = remainingBalance.multiply(monthlyInterestRate).setScale(2, RoundingMode.HALF_UP);
            BigDecimal principalAmount = monthlyRepayment.subtract(interestAmount);
            remainingBalance = remainingBalance.subtract(principalAmount).setScale(2, RoundingMode.HALF_UP);
            RepaymentBreakdownReducting repayment = new RepaymentBreakdownReducting(formattedDate, principalAmount.doubleValue(), interestAmount, principalAmount);
            breakdown.add(repayment);
        }

        return breakdown;
    }

    public static BigDecimal calculateMonthlyRepayment(double loanAmount, BigDecimal monthlyInterestRate, int loanTermMonths) {
        BigDecimal numerator = monthlyInterestRate.multiply(BigDecimal.valueOf(Math.pow(1 + monthlyInterestRate.doubleValue(), loanTermMonths)));
        BigDecimal denominator = BigDecimal.valueOf(Math.pow(1 + monthlyInterestRate.doubleValue(), loanTermMonths) - 1);
        BigDecimal repaymentAmount = BigDecimal.valueOf(loanAmount).multiply(numerator).divide(denominator, 2, RoundingMode.HALF_UP);
        return repaymentAmount;
    }
}

class RepaymentBreakdownReducting {
    private String paymentDate;
    private double repaymentAmount;
    private BigDecimal repaymentInterest;
    private BigDecimal principalPayment;

    public RepaymentBreakdownReducting(String paymentDate, double repaymentAmount, BigDecimal repaymentInterest, BigDecimal principalPayment) {
        this.paymentDate = paymentDate;
        this.repaymentAmount = repaymentAmount;
        this.repaymentInterest = repaymentInterest;
        this.principalPayment = principalPayment;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public double getRepaymentAmount() {
        return repaymentAmount;
    }

    public BigDecimal getRepaymentInterest(){
        return repaymentInterest;
    }

    public BigDecimal getPrincipalPayment(){
        return principalPayment;
    }
}
