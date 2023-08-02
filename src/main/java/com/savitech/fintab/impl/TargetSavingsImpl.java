package com.savitech.fintab.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.savitech.fintab.dto.TargetSavingsDto;
import com.savitech.fintab.entity.Account;
import com.savitech.fintab.entity.Customer;
import com.savitech.fintab.entity.TargetSavings;
import com.savitech.fintab.entity.TargetSavingsConfig;
import com.savitech.fintab.entity.User;
import com.savitech.fintab.repository.AccountRepository;
import com.savitech.fintab.repository.CustomerRepository;
import com.savitech.fintab.repository.TargetSavingsConfigRepository;
import com.savitech.fintab.repository.TargetSavingsRepository;
import com.savitech.fintab.service.TargetSavingService;
import com.savitech.fintab.util.AuthenticatedUser;
import com.savitech.fintab.util.GenerateAccountNumber;
import com.savitech.fintab.util.Response;

@Service
public class TargetSavingsImpl implements TargetSavingService{

    @Autowired
    private AuthenticatedUser authenticatedUser;

    @Autowired
    private Response response;

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

        
        if(!acceptedDays.contains(targetSavingsDto.getAutoSaveDay())){
            return response.failResponse("Invalid days of the month", HttpStatus.BAD_REQUEST);
        }

        TargetSavings targetSavings = new TargetSavings();
        TargetSavingsConfig targetSavingsConfig = new TargetSavingsConfig();

        Customer customer = customerRepository.findByUserId(user.getId());

        Account account = accountRepository.findAccountByCustomerId(customer.getId());

        targetSavingsConfig.setAutoSaveDay(targetSavingsDto.getAutoSaveDay());
        targetSavingsConfig.setAutoSaveTime(targetSavingsDto.getAutoSaveTime());
        targetSavingsConfig.setAutoSavingsAmount(Double.valueOf(targetSavingsDto.getAutoSavingsAmount()));
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
    
}
