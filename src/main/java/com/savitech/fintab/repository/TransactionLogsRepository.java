package com.savitech.fintab.repository;

import com.savitech.fintab.entity.TransactionLogs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionLogsRepository extends JpaRepository<TransactionLogs, String> {

    Page<TransactionLogs> findAllBySourceOrDestination(String source, String destination, Pageable pageable);
}
