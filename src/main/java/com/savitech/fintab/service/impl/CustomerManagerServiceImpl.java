package com.savitech.fintab.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.savitech.fintab.entity.User;
import com.savitech.fintab.repository.AccountRepository;
import com.savitech.fintab.repository.AdminUserRepository;
import com.savitech.fintab.repository.CustomerRepository;
import com.savitech.fintab.repository.UserRepository;
import com.savitech.fintab.service.CustomerManagerService;
import com.savitech.fintab.util.AuthenticatedUser;
import com.savitech.fintab.util.Response;

@Service
public class CustomerManagerServiceImpl implements CustomerManagerService{

    @Autowired
    private AuthenticatedUser authenticatedUser;

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Response response;
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public ResponseEntity<?> allCustomer(Pageable pageable) {
        User user = authenticatedUser.auth();
        if(!user.getIsAdmin()){
            return response.failResponse("You don't have permission to perform this opeation", HttpStatus.BAD_REQUEST);
        }
        userRepository.findByIsCustomer(true);
        // accountRepository.findAll();
        return ResponseEntity.ok().body(accountRepository.findAll(pageable).toList());
    }
    
}
