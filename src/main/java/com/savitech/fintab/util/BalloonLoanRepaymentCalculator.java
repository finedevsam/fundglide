package com.savitech.fintab.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BalloonLoanRepaymentCalculator {
    public static void main(String[] args) {
        double loanAmount = 10000; // Example loan amount
        double annualInterestRate = 5.0; // Example annual interest rate
        int loanTermMonths = 12; // Loan term in months
        double balloonPayment = 5000; // Balloon payment amount

        List<RepaymentBreakdown> breakdown = calculateBalloonLoanRepayment(loanAmount, annualInterestRate, loanTermMonths, balloonPayment);

        // Print the breakdown
        for (RepaymentBreakdown repayment : breakdown) {
            System.out.println("Payment Date: " + repayment.getPaymentDate() + ", Repayment Amount: $" + repayment.getRepaymentAmount()
                    + ", Principal Amount: $" + repayment.getPrincipalAmount() + ", Interest Amount: $" + repayment.getInterestAmount());
        }
    }

    public static List<RepaymentBreakdown> calculateBalloonLoanRepayment(double loanAmount, double annualInterestRate, int loanTermMonths, double balloonPayment) {
        List<RepaymentBreakdown> breakdown = new ArrayList<>();
        BigDecimal monthlyInterestRate = BigDecimal.valueOf(annualInterestRate).divide(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);
        BigDecimal monthlyPayment = calculateMonthlyPayment(loanAmount, monthlyInterestRate, loanTermMonths, balloonPayment);
        BigDecimal remainingBalance = BigDecimal.valueOf(loanAmount);
        LocalDate startDate = LocalDate.now();

        for (int i = 1; i <= loanTermMonths; i++) {
            LocalDate paymentDate = startDate.plusMonths(i);
            String formattedDate = paymentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            BigDecimal interestAmount = remainingBalance.multiply(monthlyInterestRate).setScale(2, RoundingMode.HALF_UP);
            BigDecimal principalAmount = (i == loanTermMonths) ? remainingBalance : monthlyPayment.subtract(interestAmount).setScale(2, RoundingMode.HALF_UP);
            remainingBalance = remainingBalance.subtract(principalAmount).setScale(2, RoundingMode.HALF_UP);
            RepaymentBreakdown repayment = new RepaymentBreakdown(formattedDate, monthlyPayment.doubleValue(), principalAmount.doubleValue(), interestAmount.doubleValue());
            breakdown.add(repayment);
        }

        return breakdown;
    }

    public static BigDecimal calculateMonthlyPayment(double loanAmount, BigDecimal monthlyInterestRate, int loanTermMonths, double balloonPayment) {
        BigDecimal numerator = monthlyInterestRate.multiply(BigDecimal.valueOf(Math.pow(1 + monthlyInterestRate.doubleValue(), loanTermMonths)));
        BigDecimal denominator = BigDecimal.valueOf(Math.pow(1 + monthlyInterestRate.doubleValue(), loanTermMonths) - 1);
        BigDecimal monthlyPayment = BigDecimal.valueOf(loanAmount).multiply(numerator).divide(denominator, 2, RoundingMode.HALF_UP);
        monthlyPayment = monthlyPayment.add(BigDecimal.valueOf(balloonPayment));
        return monthlyPayment;
    }
}

class RepaymentBreakdown {
    private String paymentDate;
    private double repaymentAmount;
    private double principalAmount;
    private double interestAmount;

    public RepaymentBreakdown(String paymentDate, double repaymentAmount, double principalAmount, double interestAmount) {
        this.paymentDate = paymentDate;
        this.repaymentAmount = repaymentAmount;
        this.principalAmount = principalAmount;
        this.interestAmount = interestAmount;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public double getRepaymentAmount() {
        return repaymentAmount;
    }

    public double getPrincipalAmount() {
        return principalAmount;
    }

    public double getInterestAmount() {
        return interestAmount;
    }
}

