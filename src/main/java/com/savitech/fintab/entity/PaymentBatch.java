package com.savitech.fintab.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "tbl_payment_batch")
public class PaymentBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "batch_no")
    private String batchNo;

    @Column(name = "source")
    private String sourceAccount;

    @Column(name = "payment_type", nullable = false)
    private String paymentType;

    @Column(name = "description")
    private String description;

    @Column(name = "completed", updatable = false)
    private Boolean completed = Boolean.FALSE;

    @Column(name = "payment_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @Temporal(TemporalType.TIMESTAMP)
    private Date paymentDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Timestamp create_at;
}
