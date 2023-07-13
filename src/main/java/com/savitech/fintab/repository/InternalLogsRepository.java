package com.savitech.fintab.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.savitech.fintab.entity.InternalLogs;

public interface InternalLogsRepository extends JpaRepository<InternalLogs, String>{
    
}
