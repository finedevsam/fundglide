package com.savitech.fintab.util.loan;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LoanCalculator {
    public static void main(String[] args) {
        double loanAmount = 10000; // Replace with the loan amount
        double annualInterestRate = 0.1; // Replace with the annual interest rate (e.g., 0.1 for 10%)
        int months = 12;

        List<RepaymentBreakdownInterestLoan> breakdown = calculateMonthlyRepaymentBreakdown(loanAmount, annualInterestRate, months);

        // Print the breakdown
        for (RepaymentBreakdownInterestLoan entry : breakdown) {
            System.out.println("Payment Date: " + entry.getRepaymentDate() + ", Repayment Amount: $" + entry.getMonthlyRepayment()
                    + ", Principal Amount: $" + entry.getPrincipalPayment() + ", Interest Amount: $" + entry.getInterestPayment());
        }
    }

    public static List<RepaymentBreakdownInterestLoan> calculateMonthlyRepaymentBreakdown(double loanAmount, double annualInterestRate, int months) {
        List<RepaymentBreakdownInterestLoan> breakdown = new ArrayList<>();
        BigDecimal totalLoanAmount = BigDecimal.valueOf(loanAmount);
        BigDecimal monthlyInterestRate = BigDecimal.valueOf(annualInterestRate / 12);

        // Calculate the monthly repayment amount using the formula: M = P * r * (1 + r)^n / ((1 + r)^n - 1)
        BigDecimal numerator = monthlyInterestRate.add(BigDecimal.ONE).pow(months);
        BigDecimal denominator = monthlyInterestRate.add(BigDecimal.ONE).pow(months).subtract(BigDecimal.ONE);
        BigDecimal monthlyRepayment = totalLoanAmount.multiply(monthlyInterestRate).multiply(numerator).divide(denominator, 2, RoundingMode.HALF_UP);

        // Calculate the monthly breakdown
        LocalDate currentDate = LocalDate.now();
        for (int month = 1; month <= months; month++) {
            BigDecimal interestPayment = totalLoanAmount.multiply(monthlyInterestRate).setScale(2, RoundingMode.HALF_UP);
            BigDecimal principalPayment = monthlyRepayment.subtract(interestPayment);
            BigDecimal remainingLoanAmount = totalLoanAmount.subtract(principalPayment);

            RepaymentBreakdownInterestLoan entry = new RepaymentBreakdownInterestLoan();
            entry.setRepaymentDate(currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            entry.setMonthlyRepayment(monthlyRepayment);
            entry.setPrincipalPayment(principalPayment);
            entry.setInterestPayment(interestPayment);
            entry.setRemainingLoanAmount(remainingLoanAmount);
            breakdown.add(entry);

            currentDate = currentDate.plusMonths(1);
            totalLoanAmount = remainingLoanAmount;
        }

        return breakdown;
    }
}