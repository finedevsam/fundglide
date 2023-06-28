package com.savitech.fintab.service;

import com.savitech.fintab.entity.TransactionLogs;
import com.savitech.fintab.entity.impl.PayWithChannelModel;
import com.savitech.fintab.entity.impl.Transfer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface AccountService {

    ResponseEntity<?> myAccounts();

    ResponseEntity<?> accountLookUp(String accountNo);

    ResponseEntity<?> transfer(Transfer ft);

    Page<TransactionLogs> myTransactionLogs(Pageable pageable);

    ResponseEntity<?> verifyPayWithChannel(PayWithChannelModel channelModel);
}
