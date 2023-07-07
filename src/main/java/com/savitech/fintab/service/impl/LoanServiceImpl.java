package com.savitech.fintab.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.savitech.fintab.entity.AdminUser;
import com.savitech.fintab.entity.Customer;
import com.savitech.fintab.entity.CustomerLoan;
import com.savitech.fintab.entity.CustomerLoanBreakDown;
import com.savitech.fintab.entity.LoanType;
import com.savitech.fintab.entity.User;
import com.savitech.fintab.entity.impl.ApplyForLoan;
import com.savitech.fintab.entity.impl.LoanTypeModel;
import com.savitech.fintab.repository.AdminUserRepository;
import com.savitech.fintab.repository.CustomerLoanBreakDownRepository;
import com.savitech.fintab.repository.CustomerLoanRepository;
import com.savitech.fintab.repository.CustomerRepository;
import com.savitech.fintab.repository.LoanTypeRepository;
import com.savitech.fintab.service.LoanService;
import com.savitech.fintab.util.AmortizingLoanRepaymentCalculator;
import com.savitech.fintab.util.AmortizingRepaymentBreakdown;
import com.savitech.fintab.util.AuthenticatedUser;
import com.savitech.fintab.util.Helper;
import com.savitech.fintab.util.LoanRepaymentCalculator;
import com.savitech.fintab.util.RandomStringGenerator;
import com.savitech.fintab.util.RepaymentBreakdownBasicLoan;
import com.savitech.fintab.util.Response;

import lombok.SneakyThrows;

@Service
public class LoanServiceImpl implements LoanService{

    @Autowired
    private AuthenticatedUser authenticatedUser;

    @Autowired
    private LoanTypeRepository loanTypeRepository;

    @Autowired
    private Response response;

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private RandomStringGenerator stringGenerator;

    @Autowired
    private CustomerLoanRepository customerLoanRepository;

    @Autowired
    private CustomerLoanBreakDownRepository customerLoanBreakDownRepository;

    @Autowired
    private Helper helper;

    @Autowired
    private LoanRepaymentCalculator loanRepaymentCalculator;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AmortizingLoanRepaymentCalculator amortizingLoanRepaymentCalculator;

    @Override
    public ResponseEntity<?> createLoanType(LoanTypeModel loanTypeModel) {
        User user = authenticatedUser.auth();

        if(!user.getIsAdmin()){
            return response.failResponse("You don't have permission to perform this opeation", HttpStatus.BAD_REQUEST);
        }

        AdminUser adminUser = adminUserRepository.findByUserId(user.getId());

        if(adminUser.getPermission().size() < 1){
            return response.failResponse("You don't have permission to perform this opeation", HttpStatus.BAD_REQUEST);
        }

        List<String> createPermissionlist = Arrays.asList("all");
        
        List<String> myPermission = adminUser.getPermission();
        for(String id: myPermission){
            if(!createPermissionlist.contains(helper.getRole(id))){
                return response.failResponse("You don't have permission to perform this opeation", HttpStatus.BAD_REQUEST);
            }
        }

        if(
            Objects.equals(loanTypeModel.getName(), null) || 
            Objects.equals(loanTypeModel.getName(), "") || 
            Objects.equals(loanTypeModel.getRate(), null) ||
            Objects.equals(loanTypeModel.getRate(), "") ||
            Objects.equals(loanTypeModel.getTenure(), null) ||
            Objects.equals(loanTypeModel.getTenure(), "") ||
            Objects.equals(loanTypeModel.getCode(), null) ||
            Objects.equals(loanTypeModel.getCode(), "")
        ){
            return response.failResponse("loan name, rate or tenure can't be empty", HttpStatus.BAD_REQUEST);
        }

        List<String> acceptedLoanCode = Arrays.asList("AMTZ", "BALL", "DEFF", "FIXD", "INCM", "INTR", "REDB");
        if(!acceptedLoanCode.contains(loanTypeModel.getCode())){
            return response.failResponse("Invalid loan code ['AMTZ', 'BALL', 'DEFF', 'FIXD', 'INCM', 'INTR', 'REDB']", HttpStatus.BAD_REQUEST);
        }

        LoanType loanType = new LoanType();
        loanType.setCode(loanTypeModel.getCode());
        loanType.setName(loanTypeModel.getName());
        loanType.setRate(loanTypeModel.getRate());
        loanType.setTenure(loanTypeModel.getTenure());
        loanTypeRepository.save(loanType);

        return response.successResponse("Loan type created", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> allLoan(Pageable pageable) {
        double loanAmount = 10000; // Example loan amount
        double annualInterestRate = 5.0; // Example annual interest rate
        int loanTermMonths = 12; // Loan term in months

        List<RepaymentBreakdownBasicLoan> breakdown = loanRepaymentCalculator.calculateLoanRepaymentBreakdown(loanAmount, annualInterestRate, loanTermMonths);
        for (RepaymentBreakdownBasicLoan repayment : breakdown) {
            System.out.println("Payment Date: " + repayment.getPaymentDate() + ", Repayment Amount: $" + repayment.getRepaymentAmount());
         }
        return ResponseEntity.ok().body(loanTypeRepository.findAll(pageable).toList());
    }

    @Override
    public ResponseEntity<?> updateLoanType(String Id, LoanTypeModel loanTypeModel) {
        User user = authenticatedUser.auth();

        if(!user.getIsAdmin()){
            return response.failResponse("You don't have permission to perform this opeation", HttpStatus.BAD_REQUEST);
        }

        AdminUser adminUser = adminUserRepository.findByUserId(user.getId());

        if(adminUser.getPermission().size() < 1){
            return response.failResponse("You don't have permission to perform this opeation", HttpStatus.BAD_REQUEST);
        }

        List<String> createPermissionlist = Arrays.asList("all");
        
        List<String> myPermission = adminUser.getPermission();
        for(String id: myPermission){
            if(!createPermissionlist.contains(helper.getRole(id))){
                return response.failResponse("You don't have permission to perform this opeation", HttpStatus.BAD_REQUEST);
            }
        }

        if(!loanTypeRepository.existsById(Id)){
            return response.failResponse("Invalid loan type id", HttpStatus.BAD_REQUEST);
        }

        LoanType loanType = loanTypeRepository.findLoanTypeById(Id);

        if(!Objects.equals(loanTypeModel.getName(), null) || !Objects.equals(loanTypeModel.getName(), "")){
            loanType.setName(loanTypeModel.getName());
        }

        if(!Objects.equals(loanTypeModel.getRate(), null) || !Objects.equals(loanTypeModel.getRate(), "")){
            loanType.setRate(loanTypeModel.getRate());
        }

        if(!Objects.equals(loanTypeModel.getTenure(), null) || !Objects.equals(loanTypeModel.getTenure(), "")){
            loanType.setTenure(loanTypeModel.getTenure());
        }

        if(!Objects.equals(loanTypeModel.getCode(), null) || !Objects.equals(loanTypeModel.getCode(), "")){
            List<String> acceptedLoanCode = Arrays.asList("AMTZ", "BALL", "DEFF", "FIXD", "INCM", "INTR", "REDB");
            if(!acceptedLoanCode.contains(loanTypeModel.getCode())){
                return response.failResponse("Invalid loan code. ['AMTZ', 'BALL', 'DEFF', 'FIXD', 'INCM', 'INTR', 'REDB']", HttpStatus.BAD_REQUEST);
            }
            loanType.setCode(loanTypeModel.getCode().toUpperCase());
        }
        loanTypeRepository.save(loanType);
        return response.successResponse("Loan type updated successfully", HttpStatus.OK);
    }

    @SneakyThrows
    @Override
    public ResponseEntity<?> applyForLoan(ApplyForLoan loan) {
        User user = authenticatedUser.auth();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if(!user.getIsCustomer()){
            return response.failResponse("You don't have permission to perform this opeation", HttpStatus.BAD_REQUEST);
        }
        
        if(!loanTypeRepository.existsById(loan.getLoanId())){
            return response.failResponse("Invalid loan type", HttpStatus.BAD_REQUEST);
        }

        if(Objects.equals(loan.getAmount(), "") || Objects.equals(loan.getAmount(), null)){
            return response.failResponse("Loan amount can't be empty", HttpStatus.BAD_REQUEST);
        }

        Customer customer = customerRepository.findByUserId(user.getId());

        LoanType loanType = loanTypeRepository.findLoanTypeById(loan.getLoanId());
        if(Objects.equals(loanType.getCode(), "AMTZ")){
            CustomerLoan customerLoan = new CustomerLoan();
            customerLoan.setLoanAmount(loan.getAmount());
            customerLoan.setLoanType(loanType);
            customerLoan.setLoanReference(stringGenerator.generateReference(10));
            customerLoan.setCustomer(customer);
            List<CustomerLoanBreakDown> data = new ArrayList<>();
            List<AmortizingRepaymentBreakdown> breakdowns = amortizingLoanRepaymentCalculator.calculateAmortizingLoanRepayment(Double.parseDouble(loan.getAmount()), Double.parseDouble(loanType.getRate()), Integer.parseInt(loanType.getTenure()));
            for (AmortizingRepaymentBreakdown repayment : breakdowns) {
                CustomerLoanBreakDown customerLoanBreakDown = new CustomerLoanBreakDown();
                Date date = dateFormat.parse(repayment.getPaymentDate());
                customerLoanBreakDown.setDueDate(date);
                customerLoanBreakDown.setPaymentAmount(repayment.getRepaymentAmount());
                customerLoanBreakDown.setInterest(repayment.getInterestAmount());
                customerLoanBreakDown.setCustomerLoan(customerLoan);
                customerLoanBreakDown.setCustomer(customer);
                data.add(customerLoanBreakDown);
            }
            customerLoanRepository.save(customerLoan);
            customerLoanBreakDownRepository.saveAll(data);
            return response.successResponse("Loan booked successfully and pending approval", HttpStatus.OK);
        }
        return null;
    }

    @Override
    public ResponseEntity<?> myLoans(Pageable pageable) {
        User user = authenticatedUser.auth();
        if(!user.getIsCustomer()){
            return response.failResponse("You don't have permission to perform this opeation", HttpStatus.BAD_REQUEST);
        }
        Customer customer = customerRepository.findByUserId(user.getId());

        return ResponseEntity.ok().body(customerLoanRepository.findAllCustomerLoanByCustomerId(customer.getId(), pageable));
    }

    @Override
    public ResponseEntity<?> viewLoanBreakdown(String loanId) {
        User user = authenticatedUser.auth();
        if(!user.getIsCustomer()){
            return response.failResponse("You don't have permission to perform this opeation", HttpStatus.BAD_REQUEST);
        }
        Customer customer = customerRepository.findByUserId(user.getId());

        return ResponseEntity.ok().body(customerLoanBreakDownRepository.findAllCustomerLoanBreakDownsByCustomerLoanIdAndCustomerId(loanId, customer.getId()));
    }
    
}
