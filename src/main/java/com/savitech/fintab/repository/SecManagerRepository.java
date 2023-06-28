package com.savitech.fintab.repository;

import com.savitech.fintab.entity.SecManager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecManagerRepository extends JpaRepository<SecManager, String> {
    SecManager findSecManagerByLocator(String locator);

    Boolean existsSecManagerByLocator(String locator);
}
