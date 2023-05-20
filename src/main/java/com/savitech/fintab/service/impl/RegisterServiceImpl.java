package com.savitech.fintab.service.impl;

import com.savitech.fintab.entity.Account;
import com.savitech.fintab.entity.Customer;
import com.savitech.fintab.entity.User;
import com.savitech.fintab.entity.impl.CustomerReg;
import com.savitech.fintab.repository.AccountRepository;
import com.savitech.fintab.repository.CustomerRepository;
import com.savitech.fintab.repository.UserRepository;
import com.savitech.fintab.service.RegisterService;
import com.savitech.fintab.util.GenerateAccountNumber;
import com.savitech.fintab.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private GenerateAccountNumber generateAccountNumber;

    @Autowired
    private Response response;

    @Override
    public ResponseEntity<?> register(CustomerReg reg) {
        String accountNo = generateAccountNumber.accountNumber(1);
        if (userRepository.existsByEmail(reg.getEmail())){
            return response.failResponse("Email has been used", HttpStatus.BAD_REQUEST);
        }
        User user = new User();
        Customer customer = new Customer();
        Account account = new Account();

        user.setEmail(reg.getEmail());
        user.setPassword(passwordEncoder.encode(reg.getPassword()));
        user.setIsCustomer(true);
        userRepository.save(user);

        customer.setFirstName(reg.getFirstName());
        customer.setLastName(reg.getLastName());
        customer.setPhoneNumber(reg.getPhoneNumber());
        customer.setUser(user);
        customerRepository.save(customer);

        account.setAccountNo(accountNo);
        account.setCustomer(customer);
        accountRepository.save(account);
        return response.successResponse("Account created successfully", HttpStatus.CREATED);
    }
}
