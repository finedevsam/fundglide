package com.savitech.fintab.impl;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.savitech.fintab.entity.Account;
import com.savitech.fintab.entity.CurrencyConfig;
import com.savitech.fintab.entity.Customer;
import com.savitech.fintab.repository.AccountRepository;
import com.savitech.fintab.repository.CurrencyConfigRepository;
import com.savitech.fintab.repository.CustomerRepository;
import com.savitech.fintab.service.UssdService;

@Service
public class UssdServiceImpl implements UssdService{

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CurrencyConfigRepository currencyConfigRepository;


    @Override
    public String implementUssd(String requestBody) {
        Map<String, String> body = Arrays
                .stream(requestBody.split("&"))
                .map(entry -> entry.split("="))
                .collect(Collectors.toMap(entry -> entry[0], entry -> entry.length == 2 ? entry[1] : ""));
                // String sessionId = body.get("sessionId");
                // String serviceCode = body.get("serviceCode");
                String phoneNumber = body.get("phoneNumber");
                String text = body.get("text");

                System.out.println(text);
                String newPhoneNumber = String.format("0%s", phoneNumber.substring(6));
                System.out.println(newPhoneNumber);
                StringBuilder response = new StringBuilder("");

                if(Objects.equals(customerRepository.findCustomerByPhoneNumber(newPhoneNumber), null)){
                    response.append("END Your number is not registered ");

                }else if (text.isEmpty()) {
                    // This is the first request. Note how we start the response with CON
                    
                    response.append("CON WELCOME TO FUNDGLIDE\n1. Balance\n 2. Transfer");
    
                } else if (text.contentEquals("1")) {
                    Customer customer = customerRepository.findCustomerByPhoneNumber(newPhoneNumber);
                    Account account = accountRepository.findAccountByCustomerId(customer.getId());
                    response.append(String.format("CON WELCOME TO FUNDGLIDE\n Your balance is %s %s\n\n 10. Transfer",baseCurrency(), account.getBalance()));
    
                }else if (text.contentEquals("2")) {
                    // Business logic for first level response
                    
                    // This is a terminal request. Note how we start the response with END
                    response.append("END Your phone number is ");
                    response.append(phoneNumber);
    
                } else if (text.contentEquals("1*1")) {
                    // This is a second level response where the user selected 1 in the first instance
                    
                    String accountNumber = "ACC100101";
                    response.append("END Your account number is "); // This is a terminal request. Note how we start the response with END
                    response.append(phoneNumber);
    
                }
    
                return response.toString();
    }

    private String baseCurrency(){
        String currency = null;

        CurrencyConfig currencyConfig = currencyConfigRepository.findCurrencyConfigByIsDefault(true);

        if(Objects.equals(currencyConfig, null)){
            currency = "USD";
        }else{
            currency = currencyConfig.getCode();
        }
        return currency;
    }
    
}
