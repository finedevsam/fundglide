package com.savitech.fintab.repository;

import com.savitech.fintab.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, String> {

    Optional<Account> findFirstByOrderByCreatedAtDesc();
    Boolean existsByAccountNo(String accountNo);
    Account findAccountByAccountNo(String accountNo);

    Account findAccountsByCustomerId(String id);

    Account findAccountByCustomerId(String id);

    Account findAllAccountByCustomerId(String id);

    Account findAccountByAccountNoAndCustomerId(String accountNo, String customerId);

    Account findAccountByIdAndCustomerId(String id, String customerId);
}
