package com.savitech.fintab.util.loan;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class AmortizingLoanRepaymentCalculator {

    public List<AmortizingRepaymentBreakdown> calculateAmortizingLoanRepayment(double loanAmount, double annualInterestRate, int loanTermMonths) {
        List<AmortizingRepaymentBreakdown> breakdown = new ArrayList<>();
        BigDecimal monthlyInterestRate = BigDecimal.valueOf(annualInterestRate).divide(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);
        BigDecimal monthlyPayment = calculateMonthlyPayment(loanAmount, monthlyInterestRate, loanTermMonths);
        BigDecimal remainingBalance = BigDecimal.valueOf(loanAmount);
        LocalDate startDate = LocalDate.now();

        for (int i = 1; i <= loanTermMonths; i++) {
            LocalDate paymentDate = startDate.plusMonths(i);
            String formattedDate = paymentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            BigDecimal interestAmount = remainingBalance.multiply(monthlyInterestRate).setScale(2, RoundingMode.HALF_UP);
            BigDecimal principalAmount = monthlyPayment.subtract(interestAmount).setScale(2, RoundingMode.HALF_UP);
            remainingBalance = remainingBalance.subtract(principalAmount).setScale(2, RoundingMode.HALF_UP);
            AmortizingRepaymentBreakdown repayment = new AmortizingRepaymentBreakdown();
            repayment.setInterestAmount(interestAmount.doubleValue());
            repayment.setPaymentDate(formattedDate);
            repayment.setRepaymentAmount(monthlyPayment.doubleValue());
            repayment.setPrincipalAmount(principalAmount.doubleValue());
            breakdown.add(repayment);
        }

        return breakdown;
    }

    public static BigDecimal calculateMonthlyPayment(double loanAmount, BigDecimal monthlyInterestRate, int loanTermMonths) {
        BigDecimal numerator = monthlyInterestRate.multiply(BigDecimal.valueOf(Math.pow(1 + monthlyInterestRate.doubleValue(), loanTermMonths)));
        BigDecimal denominator = BigDecimal.valueOf(Math.pow(1 + monthlyInterestRate.doubleValue(), loanTermMonths) - 1);
        BigDecimal monthlyPayment = BigDecimal.valueOf(loanAmount).multiply(numerator).divide(denominator, 2, RoundingMode.HALF_UP);
        return monthlyPayment;
    }
}
