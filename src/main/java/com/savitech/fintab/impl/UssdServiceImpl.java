package com.savitech.fintab.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.savitech.fintab.entity.Account;
import com.savitech.fintab.entity.CurrencyConfig;
import com.savitech.fintab.entity.Customer;
import com.savitech.fintab.repository.AccountRepository;
import com.savitech.fintab.repository.CurrencyConfigRepository;
import com.savitech.fintab.repository.CustomerRepository;
import com.savitech.fintab.service.UssdService;
import com.savitech.fintab.util.UssdOperation;

import lombok.SneakyThrows;

import com.savitech.fintab.util.TVSubscribtion;
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
    private UssdOperation ussdOperation;

    @Autowired
    private TVSubscribtion tvSubscribtion;


    @Override
    @SneakyThrows
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


                String[] substrings = text.split("\\*");
                List<String> data = Arrays.asList(substrings);
                System.out.println(data);
                if(Objects.equals(customerRepository.findCustomerByPhoneNumber(newPhoneNumber), null)){
                    response.append("END Your number is not registered ");

                }else if (text.isEmpty()) {
                    // This is the first request. Note how we start the response with CON
                    response.append("CON WELCOME TO FUNDGLIDE\n1. Balance\n 2. Transfer\n 3. Paybill\n 4. Quick Loan\n 0. Exit");
    
                } else if (text.contentEquals("1")) {
                    Customer customer = customerRepository.findCustomerByPhoneNumber(newPhoneNumber);
                    Account account = accountRepository.findAccountByCustomerId(customer.getId());
                    response.append(String.format("CON WELCOME TO FUNDGLIDE\n\nYour balance is %s %s\n\n 1. Transfer\n 2. Quick Loan\n 3. Paybill\n 0. Exit",baseCurrency(), account.getBalance()));
    
                }else if(text.startsWith("1*")) {
                    if(Integer.valueOf(data.get(1)) == 1){
                        if(data.size() == 2){
                            response.append("CON WELCOME TO FUNDGLIDE\n\nEnter Account NO");

                        }else if(data.size() == 3){
                            String accountNo = data.get(2);
                            Account account = accountRepository.findAccountByAccountNo(accountNo);
                            if(Objects.equals(account, null)){
                                response.append("END Wrong Account Number");
                            }else{
                                response.append(String.format("CON WELCOME TO FUNDGLIDE\n\n%s %s \n\n Enter Amount", account.getCustomer().getLastName().toUpperCase(), account.getCustomer().getFirstName().toUpperCase()));
                            }
                        }else if(data.size() == 4){
                            String accountNo = data.get(2);
                            Account account = accountRepository.findAccountByAccountNo(accountNo);
                            String amount = data.get(3);
                            response.append(String.format("CON WELCOME TO FUNDGLIDE\n\nYou are sending %s %s to %s %s \n\n Enter PIN", baseCurrency(), amount, account.getCustomer().getLastName().toUpperCase(), account.getCustomer().getFirstName().toUpperCase()));
                        }else if(data.size() == 5){

                            String pin = data.get(4);
                            String accountNo = data.get(2);
                            String amount = data.get(3);

                            Tuple<Boolean, String> resp = ussdOperation.Transfer(newPhoneNumber, accountNo, amount, pin);
                            if(resp.getItem1()){
                                response.append(String.format("END %s ", resp.getItem2()));
                            }else{
                                response.append(String.format("END %s ", resp.getItem2()));
                            }
                        }
                    }else if(Integer.valueOf(data.get(1)) == 2){
                        // Quick Loan Business Logic here
                        response.append("END Quick Loan Comming Soon");
                    }else if(Integer.valueOf(data.get(1)) == 3){
                        // Bills payment Logic here
                        response.append("END Bills Payment Comming Soon");
                    }

                }else if (text.contentEquals("2")) {
                    response.append("CON WELCOME TO FUNDGLIDE\n\nEnter Account NO");
                    
    
                } else if(text.startsWith("2*")) {
                    if(data.size() == 2 && Integer.valueOf(data.get(0))== 2){
                        String accountNo = data.get(1);
                        Account account = accountRepository.findAccountByAccountNo(accountNo);
                        if(Objects.equals(account, null)){
                            response.append("END Wrong Account Number");
                        }else{
                            response.append(String.format("CON WELCOME TO FUNDGLIDE\n\n%s %s \n\n Enter Amount", account.getCustomer().getLastName().toUpperCase(), account.getCustomer().getFirstName().toUpperCase()));
                        }
                    }else if(data.size() == 3 && Integer.valueOf(data.get(0))== 2){
                        String accountNo = data.get(1);
                        Account account = accountRepository.findAccountByAccountNo(accountNo);
                        String amount = data.get(2);
                        response.append(String.format("CON WELCOME TO FUNDGLIDE\n\nYou are sending %s %s to %s %s \n\n Enter PIN", baseCurrency(), amount, account.getCustomer().getLastName().toUpperCase(), account.getCustomer().getFirstName().toUpperCase()));
                    }else if(data.size() == 4 && Integer.valueOf(data.get(0))== 2){
                        String pin = data.get(3);
                        String accountNo = data.get(1);
                        String amount = data.get(2);

                        Tuple<Boolean, String> resp = ussdOperation.Transfer(newPhoneNumber, accountNo, amount, pin);
                        if(resp.getItem1()){
                            response.append(String.format("END %s ", resp.getItem2()));
                        }else{
                            response.append(String.format("END %s ", resp.getItem2()));
                        }
                    }

                }else if (text.contentEquals("3")) {
                    response.append("CON WELCOME TO FUNDGLIDE\n\n 1. Airtime\n 2.Data\n 3. TV\n 4. Power");

                }else if (text.startsWith("3*")){
                    if(data.size() == 2 && Integer.valueOf(data.get(1))== 1){
                        // List the Network here
                        response.append("CON WELCOME TO FUNDGLIDE\n\n 1. Airtel\n 2.MTN\n 3. Glo\n 4. 9Mobile");
                    }else if(data.size() == 2 && Integer.valueOf(data.get(1))== 2){
                        // List the Network here
                        response.append("CON WELCOME TO FUNDGLIDE\n\n 1. Airtel\n 2.MTN\n 3. Glo\n 4. 9Mobile");
                    }else if(data.size() == 2 && Integer.valueOf(data.get(1))== 3){
                        //TV Subscription here
                        response.append("CON WELCOME TO FUNDGLIDE\n\n 1. DSTV\n 2.GOTV\n 3. Startime");
                    }else if(data.size() == 2 && Integer.valueOf(data.get(1))== 4){
                        // Electricity Here
                        response.append("CON WELCOME TO FUNDGLIDE\n\n 1. Ikeja Electric\n 2.BEDC\n 3. EKO Electric\n 4.IBEDC\n 5. Abuja Electric");
                    }
                }else if (text.contentEquals("4")) {
                    response.append("END Quick Loan Comming Soon");
                }else if (text.contentEquals("0")) {
                    response.append("END Thank you for Using FUNDGLIDE");
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
