package com.savitech.fintab.util;

import org.springframework.stereotype.Service;

import java.text.DecimalFormat;

@Service
public class FloatFormat {

    public String format(float floatValue){
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return decimalFormat.format(floatValue);
    }
}
