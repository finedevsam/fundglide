package com.savitech.fintab.repository;

import com.savitech.fintab.entity.PaymentBatchDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentBatchDetailsRepository extends JpaRepository<PaymentBatchDetails, String> {
    List<PaymentBatchDetails> findAllByPaymentBatchId(String batchId);

    PaymentBatchDetails findByPaymentBatchIdAndAccountNo(String batchId, String accountNo);

    Boolean findAllByPaymentBatchIdAndProcessed(String batchId, Boolean processed);
}
