package com.savitech.fintab.service;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.savitech.fintab.dto.AddCurrencyDto;

public interface CurrencyConfigService {
    
    ResponseEntity<?> addCurrency(AddCurrencyDto addCurrencyDto);

    ResponseEntity<?> allCurrency(Pageable pageable);

    ResponseEntity<?> setDefaultCurrency(String currId);

    ResponseEntity<?> removeCurrency(String currId);
}
