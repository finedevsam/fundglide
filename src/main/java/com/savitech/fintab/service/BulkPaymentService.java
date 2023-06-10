package com.savitech.fintab.service;

import com.savitech.fintab.entity.PaymentBatch;
import com.savitech.fintab.entity.PaymentBatchDetails;
import com.savitech.fintab.entity.impl.BulkPayment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BulkPaymentService {

    ResponseEntity<?> bulkPayment(BulkPayment bulk);

    Page<PaymentBatch> allBatchPayment(Pageable pageable);

    List<PaymentBatchDetails> viewBatchDetails(String batchId);
}
