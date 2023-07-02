package com.savitech.fintab.service.impl;

import com.savitech.fintab.entity.*;
import com.savitech.fintab.entity.impl.BulkPayment;
import com.savitech.fintab.repository.*;
import com.savitech.fintab.service.BulkPaymentService;
import com.savitech.fintab.util.*;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

@Service
public class BulkPaymentServiceImpl implements BulkPaymentService {

    @Autowired
    private PaymentBatchRepository batchRepository;

    @Autowired
    private PaymentBatchDetailsRepository batchDetailsRepository;

    @Autowired
    private ManageFiles manageFiles;

    @Autowired
    private Response response;

    @Autowired
    private CredentialRepository credentialRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AuthenticatedUser authenticatedUser;

    @Autowired
    private RandomStringGenerator stringGenerator;

    @Autowired
    private Helper helper;

    @Autowired
    private AccountRepository accountRepository;

    @Value("${bank_code}")
    private String bank_code;

    @Override
    public ResponseEntity<?> bulkPayment(BulkPayment bulk) {
        DataFormatter dataFormatter = new DataFormatter();
        User user = authenticatedUser.auth();
        Credential credential = credentialRepository.findByUserId(user.getId());
        Customer customer = customerRepository.findByUserId(user.getId());
        if(!passwordEncoder.matches(bulk.getTransactionPin(), credential.getPin())){
            return response.failResponse("Invalid transaction pin", HttpStatus.BAD_REQUEST);
        }
        try {
            Account account = accountRepository.findAccountByAccountNoAndCustomerId(bulk.getDebitedAccount(), customer.getId());
            Sheet sheet = manageFiles.loadExcel(bulk.getFile());

            Pair<Boolean, String> validate = helper.checkAccountExist(sheet, bulk.getDebitedAccount());

            if(!validate.getFirst()){
                return response.failResponse(validate.getSecond(), HttpStatus.BAD_REQUEST);
            }
            if(helper.calculateSumOfExcel(sheet) > helper.workingBalance(bulk.getDebitedAccount(), customer.getId())){
                return response.failResponse("Insufficient balance", HttpStatus.BAD_REQUEST);
            }

            if(!helper.validateDateTime(bulk.getDate()) && Objects.equals(bulk.getPaymentType(), "schedule")){
                return response.failResponse("Please choose future date", HttpStatus.BAD_REQUEST);
            }
            if(Objects.equals(bulk.getPaymentType(), "instant")){
                for(Row row: sheet){
                    if(row.getRowNum() == 0){
                        continue;
                    }else{
                        String receiverAccountNo = dataFormatter.formatCellValue(row.getCell(0));
                        String amount = dataFormatter.formatCellValue(row.getCell(2));
                        String destinationBank = dataFormatter.formatCellValue(row.getCell(1));
                        
                        // Credit the Reciever
                        Account reciever = accountRepository.findAccountByAccountNo(receiverAccountNo);
                        
                        if (!accountRepository.existsByAccountNo(receiverAccountNo)){
                            return response.failResponse(String.format("%s is not a valid account number. Kindly fix and try again", receiverAccountNo), HttpStatus.BAD_REQUEST);
                        }
                        double newBal = Double.parseDouble(amount) + Double.parseDouble(reciever.getBalance());

                        reciever.setBalance(String.valueOf(newBal));
                        accountRepository.save(reciever);

                        // Debit the Sender
                        double newSenderBal = Double.parseDouble(account.getBalance()) - Double.parseDouble(amount);
                        account.setBalance(String.valueOf(newSenderBal));
                        accountRepository.save(account);

                         // Create reciever logs
                        helper.createTransactionLog(bulk.getDebitedAccount(), bank_code, receiverAccountNo, destinationBank, amount, bulk.getDescription());
                    }
                }
                return response.successResponse("Payment successfull", HttpStatus.OK);
                
            }else if(Objects.equals(bulk.getPaymentType(), "schedule")){
                PaymentBatch paymentBatch = new PaymentBatch();

                paymentBatch.setPaymentDate(helper.convertToSqlDate(bulk.getDate()));
                paymentBatch.setBatchNo(stringGenerator.generateKey(10));
                paymentBatch.setDescription(bulk.getDescription());
                paymentBatch.setPaymentType(bulk.getPaymentType());
                paymentBatch.setSourceAccount(bulk.getDebitedAccount());
                paymentBatch.setCustomer(customer);
                batchRepository.save(paymentBatch);

                List<PaymentBatchDetails> data = new ArrayList<>();
                for(Row row: sheet){
                    if(row.getRowNum() == 0){
                        continue;
                    }
                    PaymentBatchDetails obj = new PaymentBatchDetails();
                    obj.setAccountNo(dataFormatter.formatCellValue(row.getCell(0)));
                    obj.setBankCode(dataFormatter.formatCellValue(row.getCell(1)));
                    obj.setAmount(dataFormatter.formatCellValue(row.getCell(2)));
                    obj.setPaymentBatch(paymentBatch);
                    data.add(obj);
                }

                Double newLockBal = helper.calculateSumOfExcel(sheet) + Double.parseDouble(account.getLockBalance());

                account.setLockBalance(String.valueOf(newLockBal));
                accountRepository.save(account);
                batchDetailsRepository.saveAll(data);
                return response.successResponse("Payment is been processed", HttpStatus.OK);
            }else {
                return response.failResponse("Please select valid payment type", HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            return response.failResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ParseException e) {
            return response.failResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public Page<PaymentBatch> allBatchPayment(Pageable pageable) {
        User user = authenticatedUser.auth();
        Customer customer = customerRepository.findByUserId(user.getId());
        return batchRepository.findPaymentBatchByCustomerId(customer.getId(), pageable);
    }

    @Override
    public List<PaymentBatchDetails> viewBatchDetails(String batchId) {
        // User user = authenticatedUser.auth();
        // Customer customer = customerRepository.findByUserId(user.getId());
        return batchDetailsRepository.findAllByPaymentBatchId(batchId);
    }
}
