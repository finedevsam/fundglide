package com.savitech.fintab.impl;

import java.util.Arrays;
import java.util.List;
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
import com.savitech.fintab.util.Transfer;
import com.savitech.fintab.util.Tuple;

@Service
public class UssdServiceImpl implements UssdService{

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CurrencyConfigRepository currencyConfigRepository;

    @Autowired
    private Transfer transfer;


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

                // System.out.println(text);
                String newPhoneNumber = String.format("0%s", phoneNumber.substring(6));
                // System.out.println(newPhoneNumber);
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
                    response.append("CON Enter Account NO");
                    
    
                } else if(text.contains("2*")) {
                    String[] substrings = text.split("\\*");
                    List<String> data = Arrays.asList(substrings);
                    if(data.size() == 2){
                        String accountNo = data.get(1);
                        Account account = accountRepository.findAccountByAccountNo(accountNo);
                        response.append(String.format("CON %s %s \n\n Enter Amount", account.getCustomer().getLastName().toUpperCase(), account.getCustomer().getFirstName().toUpperCase()));
                    }else if(data.size() == 3){
                        String accountNo = data.get(1);
                        Account account = accountRepository.findAccountByAccountNo(accountNo);
                        String amount = data.get(2);
                        response.append(String.format("CON You are sending %s %s to %s %s \n\n Enter PIN", baseCurrency(), amount, account.getCustomer().getLastName().toUpperCase(), account.getCustomer().getFirstName().toUpperCase()));
                    }else if(data.size() == 4){
                        String pin = data.get(3);
                        String accountNo = data.get(1);
                        String amount = data.get(2);

                        Tuple<Boolean, String> resp = transfer.Transfer(newPhoneNumber, accountNo, amount, pin);
                        if(resp.getItem1()){
                            Account account = accountRepository.findAccountByAccountNo(accountNo);
                            response.append(String.format("END %s %s sent to %s %s ", baseCurrency(), amount, account.getCustomer().getLastName().toUpperCase(), account.getCustomer().getFirstName().toUpperCase()));
                        }else{
                            response.append(String.format("END %s ", resp.getItem2()));
                        }
                    }

                }
                else if (text.contentEquals("1*1")) {
                    // This is a second level response where the user selected 1 in the first instance
                    
                    // String accountNumber = "ACC100101";
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
