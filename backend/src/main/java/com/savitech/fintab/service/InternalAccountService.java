package com.savitech.fintab.service;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.savitech.fintab.dto.InternalAccountDto;

public interface InternalAccountService {
    ResponseEntity<?> createInternalAccount(InternalAccountDto internalAccountModel);
    
    ResponseEntity<?> allInternalAccount(Pageable pageable);
}
