package com.savitech.fintab.repository;

import com.savitech.fintab.entity.TransactionLogs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TransactionLogsRepository extends JpaRepository<TransactionLogs, String> {

    Page<TransactionLogs> findAllBySourceOrDestination(String source, String destination, Pageable pageable);
}
