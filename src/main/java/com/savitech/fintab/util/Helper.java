package com.savitech.fintab.util;

import com.savitech.fintab.entity.Account;
import com.savitech.fintab.entity.Customer;
import com.savitech.fintab.entity.TransactionLogs;
import com.savitech.fintab.entity.User;
import com.savitech.fintab.repository.AccountRepository;
import com.savitech.fintab.repository.CustomerRepository;
import com.savitech.fintab.repository.TransactionLogsRepository;
import com.savitech.fintab.repository.UserRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Component
public class Helper {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private FloatFormat floatFormat;

    @Autowired
    private TransactionLogsRepository transactionLogsRepository;

    @Autowired
    private RandomStringGenerator generator;

    @Value("${bank_code}")
    private String bank_code;

    @Autowired
    private EmailNotification notification;

    @Autowired
    private UserRepository userRepository;


    public double calculateSumOfExcel(Sheet sheet){
        double totalSum = 0;

        for (Row row : sheet){

            Cell cell = row.getCell(2);
            if (cell.getCellType() == CellType.NUMERIC) {
                totalSum += cell.getNumericCellValue();
            }
        }
        return totalSum;
    }

    public Pair<Boolean, String> checkAccountExist(Sheet sheet, String debitAccountNo){
        for(Row row: sheet){
            if(row.getRowNum() == 0){
                continue;
            }
            Cell bank_code = row.getCell(1);
            Cell accountNo = row.getCell(0);

            DataFormatter dataFormatter = new DataFormatter();

            if(bank_code.getCellType() == CellType._NONE || bank_code.getCellType() == CellType.BLANK){
                return Pair.of(false, "One bank code colum is either empty or none");
            }

            if(Objects.equals(dataFormatter.formatCellValue(accountNo), debitAccountNo)){
                return Pair.of(false, "You can't send money to the sender account");
            }

            if(Objects.equals(dataFormatter.formatCellValue(bank_code), "1234") && ! accountRepository.existsByAccountNo(dataFormatter.formatCellValue(accountNo))){
                return Pair.of(false, String.format("%s is not a valid account", dataFormatter.formatCellValue(accountNo)));
            }
        }
        return Pair.of(true, "all account are fine");
    }

    public Double workingBalance(String accountNo, String customerId){
        Account account = accountRepository.findAccountByAccountNoAndCustomerId(accountNo, customerId);

        return Double.parseDouble(account.getBalance()) - Double.parseDouble(account.getLockBalance());
    }


    public Date convertToSqlDate(String stringDateTime) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return formatter.parse(stringDateTime);

    }

    public static String getCellValueString(Cell cell){
        String cellValue = "";
        if(cell != null){
            switch (cell.getCellType()){
                case STRING -> cellValue = cell.getStringCellValue();
                case NUMERIC -> cellValue = String.valueOf(cell.getNumericCellValue());
                case BOOLEAN -> cellValue = String.valueOf(cell.getBooleanCellValue());
                case FORMULA -> cellValue = cell.getCellFormula();
            }
        }
        return cellValue;
    }

    public Pair<Boolean, String> processPayment(String source, String destination, String amount){
        try {
            Account sourceAcct = accountRepository.findAccountByAccountNo(source);
            Account destAcct = accountRepository.findAccountByAccountNo(destination);

            float newSourceBal = Float.parseFloat(sourceAcct.getBalance()) - Float.parseFloat(amount);
            float newDestBal = Float.parseFloat(destAcct.getBalance()) + Float.parseFloat(amount);

            float newLockBal = Float.parseFloat(sourceAcct.getLockBalance()) - Float.parseFloat(amount);

            sourceAcct.setBalance(floatFormat.format(newSourceBal));
            sourceAcct.setLockBalance(floatFormat.format(newLockBal));

            destAcct.setBalance(floatFormat.format(newDestBal));

            accountRepository.save(sourceAcct);
            accountRepository.save(destAcct);

            return Pair.of(true, "processed");
        }catch (Exception e){
            return Pair.of(false, e.getMessage());
        }
    }

    public boolean validateDateTime(String dateTimeString){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime datetime = LocalDateTime.parse(dateTimeString, formatter);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime yesterday = now.minus(1, ChronoUnit.DAYS);

        return datetime.isAfter(now) && !datetime.toLocalDate().isEqual(yesterday.toLocalDate());
    }

    public void createTransactionLog(String source, String sourceBank, String destination, String destinationBank, String amount, String description){
        TransactionLogs logs = new TransactionLogs();
        String reference = generator.generateReference(16);
        String desc = buildDescription(source, sourceBank, destination, destinationBank, description);
        logs.setReference(reference);
        logs.setSource(source);
        logs.setDestination(destination);
        logs.setAmount(amount);
        logs.setSourceBank(sourceBank);
        logs.setDestinationBank(destinationBank);
        logs.setDescription(desc);
        transactionLogsRepository.save(logs);

        // Send Email notification to destination
        sendMail(destination, reference, amount, desc, "CR");

        // Send Email notification to source
        sendMail(source, reference, amount, desc, "DR");
    }

    private String buildDescription(String sourceAccount, String sourceBank, String destinationAccount, String destinationBank, String description){
        String response = null;
        if(Objects.equals(sourceBank, bank_code) && Objects.equals(sourceBank, destinationBank)){
            Account sourceAcct = getAccount(sourceAccount);
            Account destAcct = getAccount(destinationAccount);

            Optional<Customer> sourceCustomer = getCustomer(sourceAcct.getCustomer().getId());
            Optional<Customer> destCustomer = getCustomer(destAcct.getCustomer().getId());

            response = String.format("%s|%s|%s", sourceCustomer.get().getLastName().toUpperCase(), description, destCustomer.get().getLastName().toUpperCase());
        }

        if(! Objects.equals(sourceBank, bank_code) && Objects.equals(destinationBank, bank_code)){
            Account destAcct = getAccount(destinationAccount);
            Optional<Customer> destCustomer = getCustomer(destAcct.getCustomer().getId());

            response = String.format("%s|%s", description, destCustomer.get().getLastName().toUpperCase());
        }
        return response;
    }

    private Optional<Customer> getCustomer(String customerId){
        return customerRepository.findById(customerId);
    }

    private Account getAccount(String accountNo){
        return accountRepository.findAccountByAccountNo(accountNo);
    }

    private void sendMail(String accountNo, String reference, String amount, String desc, String type){
        try {
            Account account = accountRepository.findAccountByAccountNo(accountNo);
            Customer customer = customerRepository.findCustomerById(account.getCustomer().getId());
            User user = userRepository.findUserById(customer.getUser().getId());
            Date now = new Date();
            notification.sendReceipt(customer.getFirstName(), reference, now, type, desc, amount, user.getEmail(), account.getBalance());
        }catch (Exception e){
            return;
        }
    }
}
