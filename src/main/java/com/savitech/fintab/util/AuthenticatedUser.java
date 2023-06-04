package com.savitech.fintab.util;

import com.savitech.fintab.entity.Account;
import com.savitech.fintab.entity.Customer;
import com.savitech.fintab.entity.User;
import com.savitech.fintab.repository.AccountRepository;
import com.savitech.fintab.repository.CustomerRepository;
import com.savitech.fintab.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticatedUser {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;

    public User auth(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findUserByEmail(authentication.getName());
    }

    public Customer getCustomer(User user){
        return customerRepository.findByUserId(user.getId());
    }

    public Account getCustomerAccount(Customer customer){
        return accountRepository.findAccountsByCustomerId(customer.getId());
    }
}
