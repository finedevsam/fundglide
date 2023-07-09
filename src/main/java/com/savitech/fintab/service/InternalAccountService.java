package com.savitech.fintab.service;

import org.springframework.http.ResponseEntity;

import com.savitech.fintab.entity.impl.InternalAccountModel;

public interface InternalAccountService {
    ResponseEntity<?> createInternalAccount(InternalAccountModel internalAccountModel);
}
