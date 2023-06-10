package com.savitech.fintab.repository;

import com.savitech.fintab.entity.SecManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecManagerRepository extends JpaRepository<SecManager, String> {
}
