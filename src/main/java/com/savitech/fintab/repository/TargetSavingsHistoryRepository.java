package com.savitech.fintab.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.savitech.fintab.entity.TargetSavingsHistory;

public interface TargetSavingsHistoryRepository extends JpaRepository<TargetSavingsHistory, String>{

    Page<TargetSavingsHistory> findAllTargetSavingsHistoryByTargetSavingsIdAndCustomerIdOrderByDateDesc(String targetSavingId, String customerId, Pageable pageable);
    
}
