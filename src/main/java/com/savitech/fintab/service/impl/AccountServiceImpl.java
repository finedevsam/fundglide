package com.savitech.fintab.service.impl;

import com.savitech.fintab.entity.Account;
import com.savitech.fintab.entity.Credential;
import com.savitech.fintab.entity.Customer;
import com.savitech.fintab.entity.User;
import com.savitech.fintab.entity.impl.Transfer;
import com.savitech.fintab.entity.impl.UpdateProfile;
import com.savitech.fintab.repository.AccountRepository;
import com.savitech.fintab.repository.CredentialRepository;
import com.savitech.fintab.repository.CustomerRepository;
import com.savitech.fintab.repository.UserRepository;
import com.savitech.fintab.service.AccountService;
import com.savitech.fintab.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private Response response;

    @Autowired
    private AccountType accountType;

    @Autowired
    private AuthenticatedUser authenticatedUser;

    @Autowired
    private CredentialRepository credentialRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FloatFormat floatFormat;

    @Autowired
    private UploadFile uploadFile;

    @Autowired
    private Helper helper;

    @Override
    public Account myAccounts() {
        User user = authenticatedUser.auth();
        Optional<Customer> customer = customerRepository.findCustomerByUserId(user.getId());

        return accountRepository.findAccountsByCustomerId(customer.get().getId());
    }

    @Override
    public ResponseEntity<?> accountLookUp(String accountNo) {
        Account account = accountRepository.findAccountByAccountNo(accountNo);
        if(account != null){
            Optional<Customer> customer = customerRepository.findById(account.getCustomer().getId());
            Map<Object, Object> data = new HashMap<>();
            data.put("accountName", String.format("%s %s", customer.get().getLastName().toUpperCase(),
                    customer.get().getFirstName().toUpperCase()));
            data.put("accountNo", accountNo);
            data.put("accountType", accountType.accountType(account.getCode()));
            return ResponseEntity.ok(data);
        }
        return response.failResponse("Account not found", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<?> transfer(Transfer ft) {
        User user = authenticatedUser.auth();
        Optional<Customer> customer = customerRepository.findCustomerByUserId(user.getId());
        boolean debitAccountIsNumeric = ft.getDebitAccount().matches("\\d+");
        boolean creditAccountIsNumeric = ft.getCreditAccount().matches("\\d+");
        if(!debitAccountIsNumeric || !creditAccountIsNumeric){
            return response.failResponse("Invalid debit or credit account number format", HttpStatus.BAD_REQUEST);
        }

        if(ft.getCreditAccount().length() != 10 || ft.getDebitAccount().length() != 10){
            return response.failResponse("Invalid debit or credit account number", HttpStatus.BAD_REQUEST);
        }

        if(ft.getCreditAccount().equals(ft.getDebitAccount())){
            return response.failResponse("You can't send money to the same account", HttpStatus.BAD_REQUEST);
        }

        Credential credential = credentialRepository.findByUserId(user.getId());
        boolean pinIsValid = passwordEncoder.matches(ft.getPin(), credential.getPin());
        if(pinIsValid){
            if (!accountRepository.existsByAccountNo(ft.getCreditAccount())){
                return response.failResponse("Invalid Recipient account No", HttpStatus.BAD_REQUEST);
            }

            if (!accountRepository.existsByAccountNo(ft.getDebitAccount())){
                return response.failResponse("Invalid Origin account No", HttpStatus.BAD_REQUEST);
            }
            Account debitAccount = accountRepository.findAccountByAccountNo(ft.getDebitAccount());
            if(Float.parseFloat(ft.getAmount()) > helper.workingBalance(ft.getDebitAccount(), customer.get().getId())){
                return response.failResponse("Insufficient balance", HttpStatus.BAD_REQUEST);
            }

            if(!debitAccount.getActive()){
                return response.failResponse("Account Inactive", HttpStatus.BAD_REQUEST);
            }

            Account creditAccount = accountRepository.findAccountByAccountNo(ft.getCreditAccount());


            float newdbAmount = Float.parseFloat(debitAccount.getBalance()) - Float.parseFloat(ft.getAmount());

            debitAccount.setBalance(floatFormat.format(newdbAmount));
            accountRepository.save(debitAccount);

            float newcrAmount = Float.parseFloat(creditAccount.getBalance()) + Float.parseFloat(ft.getAmount());
            creditAccount.setBalance(floatFormat.format(newcrAmount));
            accountRepository.save(creditAccount);
            return response.successResponse("Transaction successfully", HttpStatus.OK);
        }else {
            return response.failResponse("Invalid Transaction pin", HttpStatus.BAD_REQUEST);
        }
    }
}
