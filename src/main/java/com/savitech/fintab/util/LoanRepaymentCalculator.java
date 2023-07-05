package com.savitech.fintab.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class LoanRepaymentCalculator {
    
    public List<RepaymentBreakdownBasicLoan> calculateLoanRepaymentBreakdown(double loanAmount, double annualInterestRate, int loanTermMonths) {
        List<RepaymentBreakdownBasicLoan> breakdown = new ArrayList<>();
        BigDecimal monthlyInterestRate = BigDecimal.valueOf(annualInterestRate).divide(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);
        BigDecimal monthlyRepayment = calculateMonthlyRepayment(loanAmount, monthlyInterestRate, loanTermMonths);
        LocalDate startDate = LocalDate.now();

        for (int i = 1; i <= loanTermMonths; i++) {
            LocalDate paymentDate = startDate.plusMonths(i);
            String formattedDate = paymentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            double repaymentAmount = monthlyRepayment.setScale(2, RoundingMode.HALF_UP).doubleValue();

            RepaymentBreakdownBasicLoan repayment = new RepaymentBreakdownBasicLoan();
            repayment.setPaymentDate(formattedDate);
            repayment.setRepaymentAmount(repaymentAmount);
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

