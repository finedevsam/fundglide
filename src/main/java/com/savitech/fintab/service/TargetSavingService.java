package com.savitech.fintab.service;

import org.springframework.http.ResponseEntity;

import com.savitech.fintab.dto.TargetSavingsDto;

public interface TargetSavingService {
    ResponseEntity<?> createTargetSavings(TargetSavingsDto targetSavingsDto);

    ResponseEntity<?> customerTargetSavings();
}
