package com.savitech.fintab.entity;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_internal_account")
public class InternalAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

     @Column(name = "account_no")
    private String accountNo;

    private double bal;

     @Column(name = "account_code")
    private String accountCode;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    @JsonIgnore
    private Timestamp createdAt;
    
}
