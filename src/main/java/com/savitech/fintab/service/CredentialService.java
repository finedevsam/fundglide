package com.savitech.fintab.service;


import org.springframework.http.ResponseEntity;

import com.savitech.fintab.dto.SetPinDto;

public interface CredentialService {

    ResponseEntity<?> setTransactionPin(SetPinDto pin);
}
