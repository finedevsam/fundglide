package com.savitech.fintab.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.savitech.fintab.entity.TargetSavings;

public interface TargetSavingsRepository extends JpaRepository<TargetSavings, String>{
    Optional<TargetSavings> findFirstByOrderByCreatedAtDesc();

    TargetSavings findTargetSavingsByCustomerId(String customerId);
}
