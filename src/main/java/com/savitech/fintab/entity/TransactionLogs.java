package com.savitech.fintab.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "tbl_transaction_logs")
public class TransactionLogs {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String reference;

    private String source;

    @Column(name = "source_bank")
    private String sourceBank;

    private String destination;

    @Column(name = "destination_bank")
    private String destinationBank;

    private String amount;

    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Timestamp createAt;
}
