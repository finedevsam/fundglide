package com.savitech.fintab.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.savitech.fintab.repository.CustomerLoanBreakDownRepository;
import com.savitech.fintab.repository.CustomerLoanRepository;

@Component
public class ProccessLoanAutoRepayment {
    @Autowired
    private CustomerLoanBreakDownRepository customerLoanBreakDownRepository;

    @Autowired
    private CustomerLoanRepository customerLoanRepository;

    @Autowired
    private Helper helper;


    @Scheduled(cron = "1 * * * * *")
    public void processAutoRepayment() throws ParseException{
        Date today = new Date();
        String currentDate = processingDate(today);

        Date now = helper.convertToSqlDate(currentDate);
        System.out.println(now);
    }

    private String processingDate(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return formatter.format(date);
    }
}
