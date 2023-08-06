package com.savitech.fintab.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.savitech.fintab.dto.TargetSavingsDto;
import com.savitech.fintab.entity.TargetSavingsHistory;
import com.savitech.fintab.impl.TargetSavingsImpl;

@RestController
@RequestMapping("target-savings")
public class TargetSavingsController {
    
    @Autowired
    private TargetSavingsImpl targetSavingsImpl;

    @PostMapping()
    public ResponseEntity<?> createTargetSavings(@RequestBody TargetSavingsDto targetSavingsDto){
        return targetSavingsImpl.createTargetSavings(targetSavingsDto);
    }

    @GetMapping()
    public ResponseEntity<?> customerTargetSavings(){
        return targetSavingsImpl.customerTargetSavings();
    }

    @GetMapping("/{id}")
    public List<TargetSavingsHistory> myTargetSavingsHistory(@PathVariable String id, Pageable pageable){
        return targetSavingsImpl.myTargetSavingHistory(id, pageable).toList();
    }
}
