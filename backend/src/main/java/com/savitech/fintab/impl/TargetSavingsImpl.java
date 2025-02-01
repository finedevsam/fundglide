package com.savitech.fintab.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.savitech.fintab.dto.QuickSaveDto;
import com.savitech.fintab.dto.TargetSavingsDto;
import com.savitech.fintab.entity.Account;
import com.savitech.fintab.entity.Credential;
import com.savitech.fintab.entity.Customer;
import com.savitech.fintab.entity.TargetSavings;
import com.savitech.fintab.entity.TargetSavingsConfig;
import com.savitech.fintab.entity.TargetSavingsHistory;
import com.savitech.fintab.entity.User;
import com.savitech.fintab.repository.AccountRepository;
import com.savitech.fintab.repository.CredentialRepository;
import com.savitech.fintab.repository.CustomerRepository;
import com.savitech.fintab.repository.TargetSavingsConfigRepository;
import com.savitech.fintab.repository.TargetSavingsHistoryRepository;
import com.savitech.fintab.repository.TargetSavingsRepository;
import com.savitech.fintab.service.TargetSavingService;
import com.savitech.fintab.util.AuthenticatedUser;
import com.savitech.fintab.util.EmailNotification;
import com.savitech.fintab.util.GenerateAccountNumber;
import com.savitech.fintab.util.Helper;
import com.savitech.fintab.util.RandomStringGenerator;
import com.savitech.fintab.util.Response;

import lombok.SneakyThrows;

@Service
public class TargetSavingsImpl implements TargetSavingService{

    @Autowired
    private AuthenticatedUser authenticatedUser;

    @Autowired
    private Response response;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TargetSavingsRepository targetSavingsRepository;

    @Autowired
    private TargetSavingsConfigRepository targetSavingsConfigRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private GenerateAccountNumber generateAccountNumber;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private Helper helper;

    @Autowired
    private TargetSavingsHistoryRepository targetSavingsHistoryRepository;

    @Autowired
    private CredentialRepository credentialRepository;

    @Autowired
    private EmailNotification emailNotification;

    @Autowired
    private RandomStringGenerator randomStringGenerator;

    @Value("${bank_code}")
    private String bank_code;

    @Override
    public ResponseEntity<?> createTargetSavings(TargetSavingsDto targetSavingsDto) {
        User user = authenticatedUser.auth();
        if(!user.getIsCustomer()){
            return response.failResponse("Permission denied", HttpStatus.BAD_REQUEST);
        }

        List<String> acceptedDays = Arrays.asList(
            "01", "02", "03", "04", "05", "06", "07",
            "08", "09", "10", "11", "12", "13", "14", "15", 
            "16", "17", "18", "19", "20", "21", "22", "23", 
            "24", "25", "26", "27", "28", "29", "30", "31");
        

        List<String> acceptedType = Arrays.asList("daily", "weekly", "monthly");

        List<String> acceptedWeekDays = Arrays.asList("01", "02", "03", "04", "05", "06", "07");

        if(!acceptedType.contains(targetSavingsDto.getAutoSaveType())){
            return response.failResponse("Kindly select auto save type from ['daily', 'weekly', 'monthly']", HttpStatus.OK);
        }

        if(Objects.equals(targetSavingsDto.getAutoSaveType(), "weekly") && !acceptedWeekDays.contains(targetSavingsDto.getAutoSaveDay())){
            return response.failResponse("Invalid weekdays number. It can only take ['01', '02', '03', '04', '05', '06', '07']", HttpStatus.BAD_REQUEST);
        }

        if(!acceptedDays.contains(targetSavingsDto.getAutoSaveDay()) && Objects.equals(targetSavingsDto.getAutoSaveType(), "monthly") ){
            return response.failResponse("Invalid days of the month", HttpStatus.BAD_REQUEST);
        }

        TargetSavings targetSavings = new TargetSavings();
        TargetSavingsConfig targetSavingsConfig = new TargetSavingsConfig();

        Customer customer = customerRepository.findByUserId(user.getId());

        Account account = accountRepository.findAccountByCustomerId(customer.getId());

        String autoSaveDay = null;
        if(Objects.equals(targetSavingsDto.getAutoSaveType(), "daily")){
            autoSaveDay = "00";
        }else{
            autoSaveDay = targetSavingsDto.getAutoSaveDay();
        }

        targetSavingsConfig.setAutoSaveDay(autoSaveDay);
        targetSavingsConfig.setAutoSaveTime(targetSavingsDto.getAutoSaveTime());
        targetSavingsConfig.setAutoSavingsAmount(Double.valueOf(targetSavingsDto.getAutoSavingsAmount()));
        targetSavingsConfig.setAutoSaveType(targetSavingsDto.getAutoSaveType());
        targetSavingsConfig.setPrimarySource(account.getId());


        targetSavings.setAccountNo(generateAccountNumber.targetSavingsAccount(10));
        targetSavings.setCustomer(customer);
        targetSavings.setTitle(targetSavingsDto.getTitle());
        targetSavings.setTargetAmount(Double.valueOf(targetSavingsDto.getTargetAmount()));
        targetSavings.setTargetSavingsConfig(targetSavingsConfig);

        targetSavingsConfigRepository.save(targetSavingsConfig);
        targetSavingsRepository.save(targetSavings);
        
        return response.successResponse("Target savings created", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> customerTargetSavings() {
        User user = authenticatedUser.auth();
        if(!user.getIsCustomer()){
            return response.failResponse("Permission denied", HttpStatus.BAD_REQUEST);
        }

        Customer customer = customerRepository.findByUserId(user.getId());
        List<TargetSavings> targetSavings = targetSavingsRepository.findTargetSavingsByCustomerId(customer.getId());
        return ResponseEntity.ok().body(targetSavings);
    }

    @Override
    public Page<TargetSavingsHistory> myTargetSavingHistory(String targetSavingsId, Pageable pageable) {
        User user = authenticatedUser.auth();
        Customer customer = customerRepository.findByUserId(user.getId());
        return targetSavingsHistoryRepository.findAllTargetSavingsHistoryByTargetSavingsIdAndCustomerIdOrderByDateDesc(targetSavingsId, customer.getId(), pageable);
    }

    @SneakyThrows
    @Override
    public ResponseEntity<?> quickSave(String targetSavingsId, QuickSaveDto quickSaveDto) {
        Date today = new Date();
        User user = authenticatedUser.auth();
        if(!user.getIsCustomer()){
            return response.failResponse("Permission denied", HttpStatus.BAD_REQUEST);
        }

        Credential credential = credentialRepository.findByUserId(user.getId());
        boolean pinIsValid = passwordEncoder.matches(quickSaveDto.getPin(), credential.getPin());

        if(!pinIsValid){
            return response.failResponse("Invcalid Transaction Pin", HttpStatus.BAD_REQUEST);
        }

        Customer customer = customerRepository.findByUserId(user.getId());

        TargetSavings targetSavings = targetSavingsRepository.findTargetSavingsById(targetSavingsId);
        if(Objects.equals(targetSavings, null)){
            return response.failResponse("Invalid target savings id", HttpStatus.BAD_REQUEST);
        }

        Account account = accountRepository.findAccountByIdAndCustomerId(quickSaveDto.getSourceAccount(), customer.getId());

        if(Objects.equals(account, null)){
            return response.failResponse("Invalid source account", HttpStatus.BAD_REQUEST);
        }
        if(Double.valueOf(quickSaveDto.getAmount()) > Double.valueOf(account.getBalance())){
            return response.failResponse("Insufficient balance", HttpStatus.BAD_REQUEST);
        }
        
        double newAccountBal = Double.valueOf(account.getBalance()) - Double.valueOf(quickSaveDto.getAmount());

        double newSavingBal = targetSavings.getBalance() + Double.valueOf(quickSaveDto.getAmount());
        
        account.setBalance(String.valueOf(newAccountBal));
        accountRepository.save(account);

        targetSavings.setBalance(newSavingBal);
        targetSavingsRepository.save(targetSavings);

        // Create Log for source Account
        helper.createTransactionLog(
            account.getAccountNo(), 
            bank_code, targetSavings.getAccountNo(), 
            bank_code, String.valueOf(quickSaveDto.getAmount()),
            String.format("%s|AUTOSAVE", targetSavings.getTitle().toUpperCase())
            );
        
        // Create Log for Target Savings
        TargetSavingsHistory history = new TargetSavingsHistory();

        history.setAmount(quickSaveDto.getAmount());
        history.setDate(today);
        history.setTargetSavings(targetSavings);
        history.setReference(randomStringGenerator.generateReference(16));
        history.setCustomer(customer);
        targetSavingsHistoryRepository.save(history);

        // Send email notification as regards the savings
        emailNotification.AutoSaveEmail(
            customer.getFirstName(), 
            user.getEmail(),
            quickSaveDto.getAmount(), 
            targetSavings.getTitle());


        return response.successResponse(
            String.format("%s has been added to your %s savings", quickSaveDto.getAmount(), 
            targetSavings.getTitle().toUpperCase()), HttpStatus.OK);
    }
    
}
