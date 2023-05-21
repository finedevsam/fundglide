package com.savitech.fintab.repository;

import com.savitech.fintab.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    Optional<Customer> findCustomerByUserId(String id);

    Customer findByUserId(String id);
}
