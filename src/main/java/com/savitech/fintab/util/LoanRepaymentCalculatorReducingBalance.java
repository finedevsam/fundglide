package com.savitech.fintab.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class LoanRepaymentCalculatorReducingBalance {

    public List<ReducingBalanceLoanBreakDown> calculateLoanRepaymentBreakdown(double loanAmount, double annualInterestRate, int loanTermMonths) {
        List<ReducingBalanceLoanBreakDown> breakdown = new ArrayList<>();
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

            // RepaymentBreakdownReducting repayment = new RepaymentBreakdownReducting(formattedDate, principalAmount.doubleValue(), interestAmount, principalAmount);
            ReducingBalanceLoanBreakDown repayment = new ReducingBalanceLoanBreakDown();
            repayment.setPaymentDate(formattedDate);
            repayment.setRepaymentAmount(principalAmount.doubleValue());
            repayment.setRepaymentInterest(interestAmount);
            repayment.setPrincipalPayment(principalAmount);

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