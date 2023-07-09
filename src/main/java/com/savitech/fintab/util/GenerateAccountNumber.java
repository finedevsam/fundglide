package com.savitech.fintab.util;

import com.savitech.fintab.entity.Account;
import com.savitech.fintab.entity.InternalAccount;
import com.savitech.fintab.repository.AccountRepository;
import com.savitech.fintab.repository.InternalAccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Component
public class GenerateAccountNumber {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private InternalAccountRepository internalAccountRepository;

    @Transactional
    public String accountNumber(int count){
        String newAccountNo = null;

        Optional<Account> account = accountRepository.findFirstByOrderByCreatedAtDesc();

        if (account.isEmpty()) {
            newAccountNo = String.valueOf(3000000000L);
        }else {
            Long lastNumber = Long.valueOf(account.get().getAccountNo());
            for(int i = 0; i < count; i++){
                lastNumber ++;
                newAccountNo = String.valueOf(lastNumber);
            }
        }
        return newAccountNo;
    }

    public String internalAccountNumber(int count){
         String newAccountNo = null;
        Optional<InternalAccount> account = internalAccountRepository.findFirstByOrderByCreatedAtDesc();
        
        if (account.isEmpty()) {
            newAccountNo = String.valueOf(6000000001L);
        }else {
            Long lastNumber = Long.valueOf(account.get().getAccountNo());
            for(int i = 0; i < count; i++){
                lastNumber ++;
                newAccountNo = String.valueOf(lastNumber);
            }
        }
        return newAccountNo;
    }
}
