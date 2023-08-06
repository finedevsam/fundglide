package com.savitech.fintab.impl;

import com.savitech.fintab.dto.PayWithChannelDto;
import com.savitech.fintab.dto.TransferDto;
import com.savitech.fintab.entity.*;
import com.savitech.fintab.repository.*;
import com.savitech.fintab.service.AccountService;
import com.savitech.fintab.util.*;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

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
    private TransactionLogsRepository transactionLogsRepository;

    @Autowired
    private Helper helper;

    @Value("${bank_code}")
    private String bank_code;

    @Autowired
    private Encryption encryption;

    @Autowired
    private SecManagerRepository secManagerRepository;

    @Override
    public ResponseEntity<?> myAccounts() {
        User user = authenticatedUser.auth();
        if(!user.getIsCustomer()){
            return response.failResponse("Permissiion denied", HttpStatus.BAD_REQUEST);
        }

        Customer customer = customerRepository.findByUserId(user.getId());
        Account account = accountRepository.findAccountsByCustomerId(customer.getId());

        Map<Object, Object> data = new HashMap<>();
        data.put("accountNumber", account.getAccountNo());
        data.put("balance", account.getBalance());
        data.put("lockBalance", account.getLockBalance());
        data.put("tier", account.getTier());
        data.put("qrCode", account.getQRodeUrl());
        data.put("accountType", accountType.accountType(account.getCode()));

        return ResponseEntity.ok(data);
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
    public ResponseEntity<?> transfer(TransferDto ft) {
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

            // Log transaction
            helper.createTransactionLog(ft.getDebitAccount(), bank_code, ft.getCreditAccount(),
                    ft.getDestinationBankCode(), ft.getAmount(), ft.getDescription());
            return response.successResponse("Transaction successfully", HttpStatus.OK);
        }else {
            return response.failResponse("Invalid Transaction pin", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public Page<TransactionLogs> myTransactionLogs(Pageable pageable) {
        User user = authenticatedUser.auth();
        Customer customer = authenticatedUser.getCustomer(user);
        Account account = authenticatedUser.getCustomerAccount(customer);
        return transactionLogsRepository.findAllBySourceOrDestinationOrderByCreateAtDesc(account.getAccountNo(), account.getAccountNo(), pageable);
    }

    @SneakyThrows
    @Override
    public ResponseEntity<?> verifyPayWithChannel(PayWithChannelDto channelModel) {
        if(Objects.equals(channelModel.getChannel(), "qrpay")) {
            String locator = helper.getLocator(channelModel.getData());
            if (!secManagerRepository.existsSecManagerByLocator(locator)) {
                return response.failResponse("data is not recognize", HttpStatus.BAD_REQUEST);
            } else {
                SecManager secManager = secManagerRepository.findSecManagerByLocator(locator);
                byte[] passcode = secManager.getPasscode();
                Map<?, ?> decryptedData = encryption.decryptData(channelModel.getData(), passcode);
                Customer customer = customerRepository.findCustomerById(String.valueOf(decryptedData.get("cus_id")));
                Map<Object, Object> data = new HashMap<>();
                data.put("accountName", String.format("%s %s", customer.getLastName().toUpperCase(), customer.getFirstName().toUpperCase()));
                data.put("accountNo", String.valueOf(decryptedData.get("account")));
                return ResponseEntity.ok(data);
            }
        }else {
            return response.failResponse("We only support QR Pay at the moment", HttpStatus.BAD_REQUEST);
        }
    }
}
