package com.savitech.fintab.service;


import com.savitech.fintab.entity.impl.SetPin;
import org.springframework.http.ResponseEntity;

public interface CredentialService {

    ResponseEntity<?> setTransactionPin(SetPin pin);
}
