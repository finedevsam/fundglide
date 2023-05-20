package com.savitech.fintab.service;

import com.savitech.fintab.entity.Account;
import com.savitech.fintab.entity.impl.Transfer;
import org.springframework.http.ResponseEntity;

public interface AccountService {

    Account myAccounts();

    ResponseEntity<?> accountLookUp(String accountNo);

    ResponseEntity<?> transfer(Transfer ft);
}
