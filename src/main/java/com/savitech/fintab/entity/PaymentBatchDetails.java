package com.savitech.fintab.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "tbl_payment_batch_details")
public class PaymentBatchDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "account_no", nullable = false)
    private String accountNo;

    @Column(name = "bank_code", nullable = false)
    private String bankCode;

    @Column(name = "amount", nullable = false)
    private String amount;

    @Column(name = "processed")
    private Boolean processed = false;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "batch_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private PaymentBatch paymentBatch;

    @Column(name = "processed_at")
    @UpdateTimestamp
    private Timestamp processed_at;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Timestamp created_at;
}
