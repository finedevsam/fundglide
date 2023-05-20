package com.savitech.fintab.service;

import com.savitech.fintab.entity.impl.CustomerReg;
import org.springframework.http.ResponseEntity;

public interface RegisterService {

    ResponseEntity<?> register(CustomerReg reg);
}
