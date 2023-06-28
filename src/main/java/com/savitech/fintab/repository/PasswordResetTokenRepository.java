package com.savitech.fintab.repository;

import com.savitech.fintab.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, String> {
    Boolean existsByCodeAndReference(String code, String reference);

    Boolean existsByUserId(String id);

    void deleteByUserId(String id);

    PasswordResetToken findPasswordResetTokenByCodeAndReference(String code, String reference);
}
