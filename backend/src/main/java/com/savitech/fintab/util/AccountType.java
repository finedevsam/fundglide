package com.savitech.fintab.util;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AccountType {

    public String accountType(String code){
        Map<Object, Object> data = new HashMap<>();
        data.put("000", "SAVINGS ACCOUNT");
        data.put("010", "CURRENT ACCOUNT");
        data.put("012", "LOAN ACCOUNT");

        return data.get(code).toString();
    }
}
