package com.savitech.fintab.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.savitech.fintab.dto.AddCurrencyDto;
import com.savitech.fintab.dto.LoanConfigDto;
import com.savitech.fintab.impl.CurrencyConfigServiceImpl;
import com.savitech.fintab.impl.LoanServiceImpl;

@RestController
@RequestMapping("config")
public class SystemConfigController {
    
    @Autowired
    private LoanServiceImpl loanServiceImpl;

    @Autowired
    private CurrencyConfigServiceImpl currencyConfigServiceImpl;

    @GetMapping("/{service}")
    public ResponseEntity<?> systemConfiguration(@PathVariable String service){
        if(Objects.equals(service, "loan")){
            return loanServiceImpl.loanConfiguration();
        }else{
            Map<Object, Object> data = new HashMap<>();
            data.put("message", "Invalid Parameter");
            data.put("code", 400);
            data.put("status", "fail");
            return ResponseEntity.badRequest().body(data);
        }
    }

    @PostMapping("/loan")
    public ResponseEntity<?> configureLoan(@RequestBody LoanConfigDto loanConfigDto){
        return loanServiceImpl.configureLoan(loanConfigDto);
    }

    @PostMapping("/currency")
    public ResponseEntity<?> configureCurrency(@RequestBody AddCurrencyDto addCurrencyDto){
        return currencyConfigServiceImpl.addCurrency(addCurrencyDto);
    }

    @GetMapping("/currency")
    public ResponseEntity<?> allCurrency(Pageable pageable){
        return currencyConfigServiceImpl.allCurrency(pageable);
    }

    @GetMapping("/currency/{id}")
    public ResponseEntity<?> setDefaultCurrency(@PathVariable String id){
        return currencyConfigServiceImpl.setDefaultCurrency(id);
    }

    @DeleteMapping("/currency/{id}")
    public ResponseEntity<?> removeCurrency(@PathVariable String id){
        return currencyConfigServiceImpl.removeCurrency(id);
    }
}
