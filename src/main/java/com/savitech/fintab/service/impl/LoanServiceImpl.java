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
import com.savitech.fintab.util.AuthenticatedUser;
import com.savitech.fintab.util.Helper;
import com.savitech.fintab.util.RandomStringGenerator;
import com.savitech.fintab.util.Response;
import com.savitech.fintab.util.loan.AmortizingLoanRepaymentCalculator;
import com.savitech.fintab.util.loan.AmortizingRepaymentBreakdown;
import com.savitech.fintab.util.loan.BalloonLoanBreakdown;
import com.savitech.fintab.util.loan.BalloonLoanRepaymentCalculator;
import com.savitech.fintab.util.loan.DeferredLoanRepaymentCalculator;
import com.savitech.fintab.util.loan.DefferedLoanBreakDown;
import com.savitech.fintab.util.loan.FixedPrincipalLoanBreakDown;
import com.savitech.fintab.util.loan.FixedPrincipalLoanRepaymentCalculator;
import com.savitech.fintab.util.loan.IncomeDrivenLoanBreakDown;
import com.savitech.fintab.util.loan.IncomeDrivenLoanRepaymentCalculator;
import com.savitech.fintab.util.loan.InterestOnlyLoanBreakDown;
import com.savitech.fintab.util.loan.InterestOnlyLoanRepaymentCalculator;
import com.savitech.fintab.util.loan.LoanRepaymentCalculator;
import com.savitech.fintab.util.loan.LoanRepaymentCalculatorReducingBalance;
import com.savitech.fintab.util.loan.ReducingBalanceLoanBreakDown;
import com.savitech.fintab.util.loan.RepaymentBreakdownBasicLoan;

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

    @Autowired
    private BalloonLoanRepaymentCalculator balloonLoanRepaymentCalculator;

    @Autowired
    private DeferredLoanRepaymentCalculator deferredLoanRepaymentCalculator;

    @Autowired
    private FixedPrincipalLoanRepaymentCalculator fixedPrincipalLoanRepaymentCalculator;

    @Autowired
    private IncomeDrivenLoanRepaymentCalculator incomeDrivenLoanRepaymentCalculator;

    @Autowired
    private InterestOnlyLoanRepaymentCalculator interestOnlyLoanRepaymentCalculator;

    @Autowired
    private LoanRepaymentCalculatorReducingBalance reducingBalanceLoanCalculator;

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

        List<String> acceptedLoanCode = Arrays.asList("AMTZ", "BALL", "DEFF", "FIXD", "INCM", "INTR", "REDB", "BASC");
        if(!acceptedLoanCode.contains(loanTypeModel.getCode())){
            return response.failResponse("Invalid loan code ['AMTZ', 'BALL', 'DEFF', 'FIXD', 'INCM', 'INTR', 'REDB', 'BASC']", HttpStatus.BAD_REQUEST);
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

        CustomerLoan customerLoan = new CustomerLoan();

        LoanType loanType = loanTypeRepository.findLoanTypeById(loan.getLoanId());

        ResponseEntity<?> dataResponse = null;

        switch (loanType.getCode()) {
            case "AMTZ":
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
                dataResponse = response.successResponse("Loan booked successfully and pending approval", HttpStatus.OK);
            
            case "BALL":
                if(Objects.equals(loan.getOther(), "") || Objects.equals(loan.getOther(), null)){
                    return response.failResponse("Please enter the ballon payment amount", HttpStatus.BAD_REQUEST);
                }else{
                    customerLoan.setLoanAmount(loan.getAmount());
                    customerLoan.setLoanType(loanType);
                    customerLoan.setLoanReference(stringGenerator.generateReference(10));
                    customerLoan.setCustomer(customer);
                    customerLoan.setOther(loan.getOther());
                    List<CustomerLoanBreakDown> ballonData = new ArrayList<>();
                    List<BalloonLoanBreakdown> Balloonbreakdowns = balloonLoanRepaymentCalculator.calculateBalloonLoanRepayment(Double.parseDouble(loan.getAmount()), Double.parseDouble(loanType.getRate()), Integer.parseInt(loanType.getTenure()), Double.parseDouble(loan.getOther()));
                    for(BalloonLoanBreakdown repayment: Balloonbreakdowns){
                        CustomerLoanBreakDown customerLoanBreakDown = new CustomerLoanBreakDown();
                        Date date = dateFormat.parse(repayment.getPaymentDate());
                        customerLoanBreakDown.setCustomer(customer);
                        customerLoanBreakDown.setCustomerLoan(customerLoan);
                        customerLoanBreakDown.setDueDate(date);
                        customerLoanBreakDown.setInterest(repayment.getInterestAmount());
                        customerLoanBreakDown.setPaymentAmount(repayment.getRepaymentAmount());
                        ballonData.add(customerLoanBreakDown);
                    }
                    customerLoanRepository.save(customerLoan);
                    customerLoanBreakDownRepository.saveAll(ballonData);
                    dataResponse = response.successResponse("Loan booked successfully and pending approval", HttpStatus.OK);
                }
            
            case "DEFF":
                if(Objects.equals(loan.getOther(), "") || Objects.equals(loan.getOther(), null)){
                    return response.failResponse("Please enter the number of months you want to deffered the loan", HttpStatus.BAD_REQUEST);
                }else{
                    customerLoan.setLoanAmount(loan.getAmount());
                    customerLoan.setLoanType(loanType);
                    customerLoan.setLoanReference(stringGenerator.generateReference(10));
                    customerLoan.setCustomer(customer);
                    customerLoan.setOther(loan.getOther());
                    List<CustomerLoanBreakDown> defferedData = new ArrayList<>();
                    List<DefferedLoanBreakDown> defferedBreakdown = deferredLoanRepaymentCalculator.calculateDeferredLoanRepayment(Double.parseDouble(loan.getAmount()), Double.parseDouble(loanType.getRate()), Integer.parseInt(loanType.getTenure()), Integer.parseInt(loan.getOther()));
                    for(DefferedLoanBreakDown repayment : defferedBreakdown){
                        CustomerLoanBreakDown customerLoanBreakDown = new CustomerLoanBreakDown();
                        Date date = dateFormat.parse(repayment.getPaymentDate());
                        customerLoanBreakDown.setCustomer(customer);
                        customerLoanBreakDown.setCustomerLoan(customerLoan);
                        customerLoanBreakDown.setDueDate(date);
                        customerLoanBreakDown.setInterest(repayment.getInterestAmount());
                        customerLoanBreakDown.setPaymentAmount(repayment.getRepaymentAmount());
                        defferedData.add(customerLoanBreakDown);
                    }
                    customerLoanRepository.save(customerLoan);
                    customerLoanBreakDownRepository.saveAll(defferedData);
                    dataResponse = response.successResponse("Loan booked successfully and pending approval", HttpStatus.OK);
                }
            
            case "FIXD":
                customerLoan.setLoanAmount(loan.getAmount());
                customerLoan.setLoanType(loanType);
                customerLoan.setLoanReference(stringGenerator.generateReference(10));
                customerLoan.setCustomer(customer);
                List<CustomerLoanBreakDown> fixedData = new ArrayList<>();
                List<FixedPrincipalLoanBreakDown> fixedBreakDown = fixedPrincipalLoanRepaymentCalculator.calculateFixedPrincipalLoanRepayment(Double.parseDouble(loan.getAmount()), Double.parseDouble(loanType.getRate()), Integer.parseInt(loanType.getTenure()));
                for(FixedPrincipalLoanBreakDown repayment : fixedBreakDown){
                    CustomerLoanBreakDown customerLoanBreakDown = new CustomerLoanBreakDown();
                    Date date = dateFormat.parse(repayment.getPaymentDate());
                    customerLoanBreakDown.setCustomer(customer);
                    customerLoanBreakDown.setCustomerLoan(customerLoan);
                    customerLoanBreakDown.setDueDate(date);
                    customerLoanBreakDown.setPaymentAmount(repayment.getRepaymentAmount());
                    customerLoanBreakDown.setInterest(repayment.getInterestAmount());
                    fixedData.add(customerLoanBreakDown);
            
                }
                customerLoanRepository.save(customerLoan);
                customerLoanBreakDownRepository.saveAll(fixedData);
                dataResponse = response.successResponse("Loan booked successfully and pending approval", HttpStatus.OK);
            
            case "INCM":
                if(Objects.equals(loan.getOther(), "") || Objects.equals(loan.getOther(), null)){
                    return response.failResponse("Please enter the income percentage", HttpStatus.BAD_REQUEST);
                }else{
                    customerLoan.setLoanAmount(loan.getAmount());
                    customerLoan.setLoanType(loanType);
                    customerLoan.setLoanReference(stringGenerator.generateReference(10));
                    customerLoan.setCustomer(customer);
                    List<CustomerLoanBreakDown> incomeData = new ArrayList<>();
                    List<IncomeDrivenLoanBreakDown> incomeDrivenLoan = incomeDrivenLoanRepaymentCalculator.calculateIncomeDrivenLoanRepayment(Double.parseDouble(loan.getAmount()), Double.parseDouble(loanType.getRate()), Integer.parseInt(loanType.getTenure()), Double.parseDouble(loan.getOther()));
                    for(IncomeDrivenLoanBreakDown repayment : incomeDrivenLoan){
                        CustomerLoanBreakDown customerLoanBreakDown = new CustomerLoanBreakDown();
                        Date date = dateFormat.parse(repayment.getPaymentDate());
                        customerLoanBreakDown.setCustomer(customer);
                        customerLoanBreakDown.setCustomerLoan(customerLoan);
                        customerLoanBreakDown.setDueDate(date);
                        customerLoanBreakDown.setPaymentAmount(repayment.getRepaymentAmount());
                        customerLoanBreakDown.setInterest(repayment.getInterestAmount());
                        incomeData.add(customerLoanBreakDown);
                    }
                    customerLoanRepository.save(customerLoan);
                    customerLoanBreakDownRepository.saveAll(incomeData);
                    dataResponse = response.successResponse("Loan booked successfully and pending approval", HttpStatus.OK);
                }
            
            case "INTR":
                if(Objects.equals(loan.getOther(), "") || Objects.equals(loan.getOther(), null)){
                    return response.failResponse("Please enter the income percentage", HttpStatus.BAD_REQUEST);
                }else{
                    customerLoan.setLoanAmount(loan.getAmount());
                    customerLoan.setLoanType(loanType);
                    customerLoan.setLoanReference(stringGenerator.generateReference(10));
                    customerLoan.setCustomer(customer);
                    List<CustomerLoanBreakDown> interestData = new ArrayList<>();
                    List<InterestOnlyLoanBreakDown> interestOnlyLoan = interestOnlyLoanRepaymentCalculator.calculateInterestOnlyLoanRepayment(Double.parseDouble(loan.getAmount()), Double.parseDouble(loanType.getRate()), Integer.parseInt(loan.getOther()), Integer.parseInt(loanType.getTenure()));

                    for(InterestOnlyLoanBreakDown repayment : interestOnlyLoan){
                        CustomerLoanBreakDown customerLoanBreakDown = new CustomerLoanBreakDown();
                        Date date = dateFormat.parse(repayment.getPaymentDate());
                        customerLoanBreakDown.setCustomer(customer);
                        customerLoanBreakDown.setCustomerLoan(customerLoan);
                        customerLoanBreakDown.setDueDate(date);
                        customerLoanBreakDown.setPaymentAmount(repayment.getRepaymentAmount());
                        customerLoanBreakDown.setInterest(repayment.getInterestAmount());
                        interestData.add(customerLoanBreakDown);
                    }
                    customerLoanRepository.save(customerLoan);
                    customerLoanBreakDownRepository.saveAll(interestData);
                    dataResponse = response.successResponse("Loan booked successfully and pending approval", HttpStatus.OK);
                }
            case "REDB":
                customerLoan.setLoanAmount(loan.getAmount());
                customerLoan.setLoanType(loanType);
                customerLoan.setLoanReference(stringGenerator.generateReference(10));
                customerLoan.setCustomer(customer);
                List<CustomerLoanBreakDown> reducingData = new ArrayList<>();
                List<ReducingBalanceLoanBreakDown> reducingBalance = reducingBalanceLoanCalculator.calculateLoanRepaymentBreakdown(Double.parseDouble(loan.getAmount()), Double.parseDouble(loanType.getRate()), Integer.parseInt(loanType.getTenure()));
                for(ReducingBalanceLoanBreakDown repayment : reducingBalance){
                    CustomerLoanBreakDown customerLoanBreakDown = new CustomerLoanBreakDown();
                    Date date = dateFormat.parse(repayment.getPaymentDate());
                    customerLoanBreakDown.setCustomer(customer);
                    customerLoanBreakDown.setCustomerLoan(customerLoan);
                    customerLoanBreakDown.setDueDate(date);
                    customerLoanBreakDown.setInterest(repayment.getRepaymentInterest().doubleValue());
                    customerLoanBreakDown.setPaymentAmount(repayment.getRepaymentAmount());
                    reducingData.add(customerLoanBreakDown);

                }
                customerLoanRepository.save(customerLoan);
                customerLoanBreakDownRepository.saveAll(reducingData);
                dataResponse = response.successResponse("Loan booked successfully and pending approval", HttpStatus.OK);
            case "BASC":
                customerLoan.setLoanAmount(loan.getAmount());
                customerLoan.setLoanType(loanType);
                customerLoan.setLoanReference(stringGenerator.generateReference(10));
                customerLoan.setCustomer(customer);
                List<CustomerLoanBreakDown> BasicData = new ArrayList<>();
                List<RepaymentBreakdownBasicLoan> basicLoan = loanRepaymentCalculator.calculateLoanRepaymentBreakdown(Double.parseDouble(loan.getAmount()), Double.parseDouble(loanType.getRate()), Integer.parseInt(loanType.getTenure()));
                for(RepaymentBreakdownBasicLoan repayment : basicLoan){
                    CustomerLoanBreakDown customerLoanBreakDown = new CustomerLoanBreakDown();
                    Date date = dateFormat.parse(repayment.getPaymentDate());
                    customerLoanBreakDown.setCustomer(customer);
                    customerLoanBreakDown.setCustomerLoan(customerLoan);
                    customerLoanBreakDown.setDueDate(date);
                    customerLoanBreakDown.setPaymentAmount(repayment.getRepaymentAmount());
                    customerLoanBreakDown.setInterest(repayment.getInterestAmount());
                    BasicData.add(customerLoanBreakDown);
                }
                customerLoanRepository.save(customerLoan);
                customerLoanBreakDownRepository.saveAll(BasicData);
                dataResponse = response.successResponse("Loan booked successfully and pending approval", HttpStatus.OK);
            default:
                dataResponse = response.failResponse("something went wrong", HttpStatus.BAD_REQUEST);
        }
        return dataResponse;
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

    @Override
    public ResponseEntity<?> adminAllCustomerLoan(Pageable pageable) {
        User user = authenticatedUser.auth();
        if(!user.getIsAdmin()){
            return response.failResponse("You don't have permission to perform this opeation", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().body(customerLoanRepository.findAll(pageable).toList());
    }

    @Override
    public ResponseEntity<?> adminViewLoanBreakdown(String loanId) {
        User user = authenticatedUser.auth();
        if(!user.getIsAdmin()){
            return response.failResponse("You don't have permission to perform this opeation", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().body(customerLoanBreakDownRepository.findAllCustomerLoanBreakDownsByCustomerLoanId(loanId));
    }

    @Override
    public ResponseEntity<?> approveLoan(String loanId) {
        User user = authenticatedUser.auth();
        if(!user.getIsAdmin()){
            return response.failResponse("You don't have permission to perform this opeation", HttpStatus.BAD_REQUEST);
        }
        AdminUser adminUser = adminUserRepository.findByUserId(user.getId());

        if(adminUser.getPermission().size() < 1){
            return response.failResponse("You don't have permission to perform this opeation", HttpStatus.BAD_REQUEST);
        }

        List<String> createPermissionlist = Arrays.asList("all", "authoriser");
        
        List<String> myPermission = adminUser.getPermission();
        for(String id: myPermission){
            if(!createPermissionlist.contains(helper.getRole(id))){
                return response.failResponse("You don't have permission to perform this opeation", HttpStatus.BAD_REQUEST);
            }
        }
        CustomerLoan customerLoan = customerLoanRepository.findCustomerLoanById(loanId);
        if(Objects.equals(customerLoan, null)){
            return response.failResponse("invalid id", HttpStatus.BAD_REQUEST);
        }

        if(Objects.equals(customerLoan.isApproved(), true)){
            return response.failResponse("Loan application has already been approved", HttpStatus.BAD_REQUEST);
        }

        customerLoan.setApproved(true);
        customerLoan.setApprovedBy(String.format("%s %s", adminUser.getLastName().toUpperCase(), adminUser.getFirstName().toUpperCase()));
        customerLoanRepository.save(customerLoan);
        return response.successResponse("loan approved successfully", HttpStatus.OK);
    }
    
}
