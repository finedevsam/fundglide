package com.savitech.fintab.service;

import org.springframework.http.ResponseEntity;

import com.savitech.fintab.dto.CustomerRegDto;

public interface RegisterService {

    ResponseEntity<?> register(CustomerRegDto reg);
}
