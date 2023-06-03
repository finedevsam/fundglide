package com.savitech.fintab.repository;

import com.savitech.fintab.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

    Optional<Account> findFirstByOrderByCreatedAtDesc();
    Boolean existsByAccountNo(String accountNo);
    Account findAccountByAccountNo(String accountNo);

    Account findAccountsByCustomerId(String id);

    Account findAccountByCustomerId(String id);

    Account findAccountByAccountNoAndCustomerId(String accountNo, String customerId);
}
