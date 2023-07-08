package com.savitech.fintab.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class IncomeDrivenLoanRepaymentCalculator {
    
    public List<IncomeDrivenLoanBreakDown> calculateIncomeDrivenLoanRepayment(double loanAmount, double annualInterestRate, int loanTermMonths, double incomePercentage) {
        List<IncomeDrivenLoanBreakDown> breakdown = new ArrayList<>();
        BigDecimal monthlyInterestRate = BigDecimal.valueOf(annualInterestRate).divide(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);
        BigDecimal remainingBalance = BigDecimal.valueOf(loanAmount);
        LocalDate startDate = LocalDate.now();

        for (int i = 1; i <= loanTermMonths; i++) {
            LocalDate paymentDate = startDate.plusMonths(i);
            String formattedDate = paymentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            BigDecimal interestAmount = remainingBalance.multiply(monthlyInterestRate).setScale(2, RoundingMode.HALF_UP);
            BigDecimal repaymentAmount = remainingBalance.multiply(BigDecimal.valueOf(incomePercentage)).setScale(2, RoundingMode.HALF_UP);
            BigDecimal principalAmount = repaymentAmount.subtract(interestAmount).setScale(2, RoundingMode.HALF_UP);
            remainingBalance = remainingBalance.subtract(principalAmount).setScale(2, RoundingMode.HALF_UP);
            
            IncomeDrivenLoanBreakDown repayment = new IncomeDrivenLoanBreakDown();
            repayment.setPaymentDate(formattedDate);
            repayment.setRepaymentAmount(repaymentAmount.doubleValue());
            repayment.setPrincipalAmount(principalAmount.doubleValue());
            repayment.setInterestAmount(interestAmount.doubleValue());
            breakdown.add(repayment);
        }

        return breakdown;
    }
}

