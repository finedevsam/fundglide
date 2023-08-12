package com.savitech.fintab.util;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.savitech.fintab.entity.Account;
import com.savitech.fintab.entity.Credential;
import com.savitech.fintab.entity.Customer;
import com.savitech.fintab.repository.AccountRepository;
import com.savitech.fintab.repository.CredentialRepository;
import com.savitech.fintab.repository.CustomerRepository;

@Component
public class Transfer {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CredentialRepository credentialRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Helper helper;

    @Value("${bank_code}")
    private String bank_code;

    public Tuple<Boolean, String> Transfer(String newPhoneNumber, String recieverAccount, String amount, String pin){
        Customer customer = customerRepository.findCustomerByPhoneNumber(newPhoneNumber);
        Account account = accountRepository.findAccountByCustomerId(customer.getId());

        Account reciepient = accountRepository.findAccountByAccountNo(recieverAccount);

        Credential credential = credentialRepository.findByUserId(customer.getUser().getId());
        boolean pinIsValid = passwordEncoder.matches(pin, credential.getPin());
        if(pinIsValid){
            double senderBal = Double.parseDouble(account.getBalance());
            double amountToSend = Double.parseDouble(amount);
            double receiverBal = Double.parseDouble(reciepient.getBalance());

            if(Objects.equals(account.getAccountNo(), recieverAccount)){
                return new Tuple<>(false, "you can't send money to yourself");
            }

            if(amountToSend > senderBal){
                return new Tuple<>(false, "Insufficient Balance");
            }

            double newSendBal = senderBal - amountToSend;
            double newRecBal = receiverBal + amountToSend;

            account.setBalance(String.valueOf(newSendBal));
            accountRepository.save(account);

            reciepient.setBalance(String.valueOf(newRecBal));
            accountRepository.save(reciepient);
            
            // Log transaction
            helper.createTransactionLog(account.getAccountNo(), bank_code, recieverAccount,
                bank_code, amount, "USSD");

            return new Tuple<>(true, "Transfer Successfull");
        }else{
            return new Tuple<>(false, "Invalid transaction pin");
        }
    }
}
