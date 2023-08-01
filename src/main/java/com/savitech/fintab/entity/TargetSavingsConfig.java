package com.savitech.fintab.entity;

import java.sql.Date;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "tbl_target_savings_config")
public class TargetSavingsConfig {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "primary_source", nullable = true)
    private String primarySource;

    @Column(name = "secondary_source", nullable = true)
    private String secondarySource;

    @Column(name = "auto_savings_amount", nullable = true)
    private double autoSavingsAmount;

    @Column(name = "auto_save_date", nullable = true)
    private Date autoSaveDate;

    @Column(name = "auto_save")
    private boolean autoSave = Boolean.FALSE;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_savings_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TargetSavings targetSavings;

}
