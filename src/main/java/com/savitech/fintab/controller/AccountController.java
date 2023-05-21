package com.savitech.fintab.controller;

import com.savitech.fintab.entity.Account;
import com.savitech.fintab.entity.impl.SetPin;
import com.savitech.fintab.entity.impl.Transfer;
import com.savitech.fintab.service.impl.AccountServiceImpl;
import com.savitech.fintab.service.impl.CredentialServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("account")
public class AccountController {

    @Autowired
    private AccountServiceImpl accountService;

    @Autowired
    private CredentialServiceImpl credentialService;

    @GetMapping()
    public Account account(){
        return accountService.myAccounts();
    }



    @GetMapping("/lookup/{accountNo}")
    public ResponseEntity<?> accountLookup(@PathVariable String accountNo){
        return accountService.accountLookUp(accountNo);
    }

    @PostMapping("/set-pin")
    public ResponseEntity<?> setPin(@RequestBody SetPin setPin){
        return credentialService.setTransactionPin(setPin);
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> fundTransfer(@RequestBody Transfer ft){
        return accountService.transfer(ft);
    }
}
