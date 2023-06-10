package com.savitech.fintab.repository;

import com.savitech.fintab.entity.PaymentBatch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PaymentBatchRepository extends JpaRepository<PaymentBatch, String> {

    List<PaymentBatch> findAllByCompletedAndPaymentDate(Boolean completed, Date paymentDate);

    PaymentBatch findPaymentBatchByBatchNo(String batchNo);

    Page<PaymentBatch> findPaymentBatchByCustomerId(String customerId, Pageable pageable);
}
