package com.savitech.fintab.service;

import com.savitech.fintab.entity.impl.BulkPayment;
import org.springframework.http.ResponseEntity;

public interface BulkPaymentService {

    ResponseEntity<?> bulkPayment(BulkPayment bulk);
}
