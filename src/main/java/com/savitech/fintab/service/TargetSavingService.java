package com.savitech.fintab.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.savitech.fintab.dto.TargetSavingsDto;
import com.savitech.fintab.entity.TargetSavingsHistory;

public interface TargetSavingService {
    ResponseEntity<?> createTargetSavings(TargetSavingsDto targetSavingsDto);

    ResponseEntity<?> customerTargetSavings();

    Page<TargetSavingsHistory> myTargetSavingHistory(String targetSavingsId, Pageable pageable);

}
