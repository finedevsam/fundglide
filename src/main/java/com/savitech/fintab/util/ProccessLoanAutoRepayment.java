package com.savitech.fintab.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.savitech.fintab.entity.Account;
// import com.savitech.fintab.entity.Customer;
import com.savitech.fintab.entity.CustomerLoan;
import com.savitech.fintab.entity.CustomerLoanBreakDown;
import com.savitech.fintab.entity.InternalAccount;
import com.savitech.fintab.entity.LoanConfig;
import com.savitech.fintab.repository.AccountRepository;
import com.savitech.fintab.repository.CustomerLoanBreakDownRepository;
import com.savitech.fintab.repository.CustomerLoanRepository;
// import com.savitech.fintab.repository.CustomerRepository;
import com.savitech.fintab.repository.InternalAccountRepository;
import com.savitech.fintab.repository.LoanConfigRepository;

@Component
public class ProccessLoanAutoRepayment {
    @Autowired
    private CustomerLoanBreakDownRepository customerLoanBreakDownRepository;

    @Autowired
    private CustomerLoanRepository customerLoanRepository;

    // @Autowired
    // private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private InternalAccountRepository internalAccountRepository;

    @Autowired
    private LoanConfigRepository loanConfigRepository;

    @Autowired
    private Helper helper;

    @Value("${bank_code}")
    private String bank_code;


    @Scheduled(cron = "1 * * * * *")
    public void processAutoRepayment() throws ParseException{
        Date today = new Date();
        String currentDate = processingDate(today);

        Date now = helper.convertToSqlDate(currentDate);
        List<CustomerLoanBreakDown> loanBreakDowns = customerLoanBreakDownRepository.findAllCustomerLoanBreakDownByPaidAndDueDate(false, now);
        for(CustomerLoanBreakDown breakDown : loanBreakDowns){
            CustomerLoan customerLoan = customerLoanRepository.findCustomerLoanById(breakDown.getCustomerLoan().getId());
            // Customer customer = customerRepository.findCustomerById(breakDown.getCustomer().getId());
            Account account = accountRepository.findAccountByCustomerId(breakDown.getCustomer().getId());

            LoanConfig loanConfigDisburse = loanConfigRepository.findLoanConfigByType("disburse");
            LoanConfig loanConfigInterest = loanConfigRepository.findLoanConfigByType("interest");

            InternalAccount internalAccountDisburse = internalAccountRepository.findInternalAccountById(loanConfigDisburse.getSourceId());

            InternalAccount internalAccountInterest = internalAccountRepository.findInternalAccountById(loanConfigInterest.getSourceId());

            if(breakDown.getPaymentAmount() > Double.parseDouble(account.getBalance())){
                continue;
            }

            Pair<Double, Double> calculate = calculatePaymentAndInterest(breakDown.getInterest(), breakDown.getPaymentAmount());

            // Debit Customer
            double newCustomerBal = Double.parseDouble(account.getBalance()) - breakDown.getPaymentAmount();
            account.setBalance(String.valueOf(newCustomerBal));
            accountRepository.save(account);

            // Create Transaction Logs for customer

            // Initial payment log
            helper.createTransactionLog(account.getAccountNo(), bank_code, internalAccountDisburse.getAccountNo(), bank_code, String.valueOf(calculate.getFirst()), "Loan Repayment");

            // Interest payment log
            helper.createTransactionLog(account.getAccountNo(), bank_code, internalAccountInterest.getAccountNo(), bank_code, String.valueOf(calculate.getSecond()), "Loan Interest");

            // Credit Internal Account(Disburse & Interest)

            double newInternalDisburseBal = internalAccountDisburse.getBal() + calculate.getFirst();
            double newInternalInterest = internalAccountInterest.getBal() + calculate.getSecond();

            internalAccountDisburse.setBal(newInternalDisburseBal);
            internalAccountInterest.setBal(newInternalInterest);

            internalAccountRepository.save(internalAccountDisburse);
            internalAccountRepository.save(internalAccountInterest);

            // Internal Logs
            // Capital Loan Payment
            helper.createInternalLogs(account.getAccountNo(), internalAccountDisburse.getAccountNo(), calculate.getFirst(), "Initial Payment", customerLoan.getLoanReference());

            // Interest Payment
            helper.createInternalLogs(account.getAccountNo(), internalAccountInterest.getAccountNo(), calculate.getSecond(), "Interest Payment", customerLoan.getLoanReference());


            // Close the Loan after completion of the Loan
            breakDown.setPaid(true);
            customerLoanBreakDownRepository.save(breakDown);

        }
    }

    private Pair<Double, Double> calculatePaymentAndInterest(double interest, double payment){

        if(interest < 0){
            double paymentAmount = interest + payment;
            double newInterest = payment - paymentAmount;
            return Pair.of(paymentAmount, newInterest);
        }else{
            double paymentAmount = payment - interest;
            return Pair.of(paymentAmount, interest);
        }
    }

    private String processingDate(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return formatter.format(date);
    }
}
