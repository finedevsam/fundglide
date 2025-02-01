package com.savitech.fintab.repository;

import com.savitech.fintab.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, String> {
    Optional<Customer> findCustomerByUserId(String id);

    Customer findByUserId(String id);

    Customer findCustomerById(String id);

    Customer findCustomerByPhoneNumber(String number);
}
