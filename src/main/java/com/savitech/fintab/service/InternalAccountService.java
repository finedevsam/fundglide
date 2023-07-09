package com.savitech.fintab.service;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.savitech.fintab.entity.impl.InternalAccountModel;

public interface InternalAccountService {
    ResponseEntity<?> createInternalAccount(InternalAccountModel internalAccountModel);
    
    ResponseEntity<?> allInternalAccount(Pageable pageable);
}
