package com.savitech.fintab.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.savitech.fintab.entity.CurrencyConfig;

public interface CurrencyConfigRepository extends JpaRepository<CurrencyConfig, String>{
    boolean existsByCode(String code);

    boolean existsByIsDefault(Boolean isDefault);

    CurrencyConfig findCurrencyConfigById(String id);

    CurrencyConfig findCurrencyConfigByIsDefault(Boolean isDefault);
}
