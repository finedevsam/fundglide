package com.savitech.fintab.service;

import com.savitech.fintab.dto.PayWithChannelDto;
import com.savitech.fintab.dto.TransferDto;
import com.savitech.fintab.entity.TransactionLogs;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface AccountService {

    ResponseEntity<?> myAccounts();

    ResponseEntity<?> accountLookUp(String accountNo);

    ResponseEntity<?> transfer(TransferDto ft);

    Page<TransactionLogs> myTransactionLogs(Pageable pageable);

    ResponseEntity<?> verifyPayWithChannel(PayWithChannelDto channelModel);
}
