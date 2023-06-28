package com.savitech.fintab.repository;

import com.savitech.fintab.entity.Credential;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CredentialRepository extends JpaRepository<Credential, String> {

    Credential findByUserId(String id);
}
