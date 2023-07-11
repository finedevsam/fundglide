package com.savitech.fintab.service;

import com.savitech.fintab.dto.BulkPaymentDto;
import com.savitech.fintab.entity.PaymentBatch;
import com.savitech.fintab.entity.PaymentBatchDetails;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BulkPaymentService {

    ResponseEntity<?> bulkPayment(BulkPaymentDto bulk);

    Page<PaymentBatch> allBatchPayment(Pageable pageable);

    List<PaymentBatchDetails> viewBatchDetails(String batchId);
}
