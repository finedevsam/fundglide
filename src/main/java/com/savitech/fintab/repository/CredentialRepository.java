package com.savitech.fintab.repository;

import com.savitech.fintab.entity.Credential;
import com.savitech.fintab.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CredentialRepository extends JpaRepository<Credential, String> {

    Credential findByUserId(String id);
}
