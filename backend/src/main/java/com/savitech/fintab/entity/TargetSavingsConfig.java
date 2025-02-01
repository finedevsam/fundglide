package com.savitech.fintab.entity;

import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    @Column(name = "auto_save_type", nullable = true)
    private String autoSaveType;

    @Column(name = "auto_savings_amount", nullable = true)
    private double autoSavingsAmount;

    @Column(name = "auto_save_day", nullable = true)
    private String autoSaveDay;

    @Column(name = "auto_save_time")
    private LocalTime autoSaveTime;

    @Column(name = "auto_save")
    private boolean autoSave = Boolean.TRUE;

}
