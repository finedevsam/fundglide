package com.savitech.fintab.service;

import com.savitech.fintab.entity.Account;
import com.savitech.fintab.entity.TransactionLogs;
import com.savitech.fintab.entity.impl.Transfer;
import com.savitech.fintab.entity.impl.UpdateProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface AccountService {

    Account myAccounts();

    ResponseEntity<?> accountLookUp(String accountNo);

    ResponseEntity<?> transfer(Transfer ft);

    Page<TransactionLogs> myTransactionLogs(Pageable pageable);
}
