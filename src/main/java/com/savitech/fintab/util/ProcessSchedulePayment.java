package com.savitech.fintab.util;


import com.savitech.fintab.entity.PaymentBatch;
import com.savitech.fintab.entity.PaymentBatchDetails;
import com.savitech.fintab.repository.PaymentBatchDetailsRepository;
import com.savitech.fintab.repository.PaymentBatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Component
public class ProcessSchedulePayment {

    @Autowired
    private PaymentBatchRepository paymentBatchRepository;

    @Autowired
    private PaymentBatchDetailsRepository paymentBatchDetailsRepository;

    @Autowired
    private Helper helper;


    @Scheduled(cron = "1 * * * * *")
    public void process() throws ParseException {
        Date today = new Date();
        String currentDate = processingDate(today);

        Date now = helper.convertToSqlDate(currentDate);
        List<PaymentBatch> paymentBatch = paymentBatchRepository.findAllByCompletedAndPaymentDate(false, now);
        for(PaymentBatch batch : paymentBatch){
            List<PaymentBatchDetails> paymentBatchDetails = paymentBatchDetailsRepository.findAllByPaymentBatchId(batch.getId());
            for(PaymentBatchDetails pbd : paymentBatchDetails) {

                Pair<Boolean, String> process = helper.processPayment(batch.getSourceAccount(), pbd.getAccountNo(), pbd.getAmount());
                if(process.getFirst()){
                    PaymentBatchDetails paymentBatchDetails1 = paymentBatchDetailsRepository.findByPaymentBatchIdAndAccountNo(batch.getId(), pbd.getAccountNo());
                    paymentBatchDetails1.setProcessed(true);
                    paymentBatchDetailsRepository.save(paymentBatchDetails1);
                }
            }
            updateBatchProcess(batch.getBatchNo());
        }
    }

    private String processingDate(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return formatter.format(date);
    }

    private void updateBatchProcess(String batchNo){
        PaymentBatch batch = paymentBatchRepository.findPaymentBatchByBatchNo(batchNo);
        batch.setCompleted(true);
        paymentBatchRepository.save(batch);
    }
}
