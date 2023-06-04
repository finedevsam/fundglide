package com.savitech.fintab.repository;

import com.savitech.fintab.entity.TransactionLogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionLogsRepository extends JpaRepository<TransactionLogs, String> {
}
