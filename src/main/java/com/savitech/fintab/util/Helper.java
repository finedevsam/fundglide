package com.savitech.fintab.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.savitech.fintab.entity.Account;
import com.savitech.fintab.entity.Customer;
import com.savitech.fintab.entity.CustomerLoan;
import com.savitech.fintab.entity.InternalAccount;
import com.savitech.fintab.entity.InternalLogs;
import com.savitech.fintab.entity.LoanConfig;
import com.savitech.fintab.entity.Permission;
import com.savitech.fintab.entity.TransactionLogs;
import com.savitech.fintab.entity.User;
import com.savitech.fintab.repository.AccountRepository;
import com.savitech.fintab.repository.CustomerRepository;
import com.savitech.fintab.repository.InternalAccountRepository;
import com.savitech.fintab.repository.InternalLogsRepository;
import com.savitech.fintab.repository.LoanConfigRepository;
import com.savitech.fintab.repository.PermissionRepository;
import com.savitech.fintab.repository.TransactionLogsRepository;
import com.savitech.fintab.repository.UserRepository;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.awt.Color;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
public class Helper {

    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;

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

    @Autowired
    private AmountToWords amountToWords;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private LoanConfigRepository loanConfigRepository;


    @Autowired
    private InternalAccountRepository internalAccountRepository;

    @Autowired
    private InternalLogsRepository internalLogsRepository;


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

        String newDescription = null;
        String desc = buildDescription(source, sourceBank, destination, destinationBank, description);
        if(!Objects.equals(desc, null)){
            newDescription = desc;
        }else{
            newDescription = description;
        }

        logs.setReference(reference);
        logs.setSource(source);
        logs.setDestination(destination);
        logs.setAmount(amount);
        logs.setSourceBank(sourceBank);
        logs.setDestinationBank(destinationBank);
        logs.setDescription(newDescription);
        transactionLogsRepository.save(logs);
        String amt = StringUtils.capitalize(amountToWords.convertToWords(Double.parseDouble(amount)));
        System.out.println(amt);
        // Send Email notification to destination
        if(!Objects.equals(desc, null)){
            sendMail(destination, reference, amount, newDescription, "CR");
        }

        // Send Email notification to source
        if(!Objects.equals(desc, null)){
            sendMail(source, reference, amount, newDescription, "DR");
        }
    }

    public void createInternalLogs(String source, String destination, double amount, String description, String loanReference){
        InternalLogs logs = new InternalLogs();
        String reference = generator.generateReference(16);
        logs.setAmount(amount);
        logs.setDescription(description);
        logs.setReference(reference);
        logs.setSource(source);
        logs.setDestination(destination);
        logs.setLoanReference(loanReference);
        internalLogsRepository.save(logs);
    }

    private String buildDescription(String sourceAccount, String sourceBank, String destinationAccount, String destinationBank, String description){
        String response = null;
        if(Objects.equals(sourceBank, bank_code) && Objects.equals(sourceBank, destinationBank)){
            try {
                Account sourceAcct = getAccount(sourceAccount);
                Account destAcct = getAccount(destinationAccount);

                Optional<Customer> sourceCustomer = getCustomer(sourceAcct.getCustomer().getId());
                Optional<Customer> destCustomer = getCustomer(destAcct.getCustomer().getId());

                response = String.format("%s|%s|%s", sourceCustomer.get().getLastName().toUpperCase(), description, destCustomer.get().getLastName().toUpperCase());
                
            } catch (Exception e) {
                response = null;
            }
            
        }

        if(! Objects.equals(sourceBank, bank_code) && Objects.equals(destinationBank, bank_code)){
            try {
                Account destAcct = getAccount(destinationAccount);
                Optional<Customer> destCustomer = getCustomer(destAcct.getCustomer().getId());

                response = String.format("%s|%s", description, destCustomer.get().getLastName().toUpperCase());
                
            } catch (Exception e) {
                response = null;
            }
            
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

    public BufferedImage generateQRCodeImage(String qrCodeData, int qrCodeSize) {
        try {
            // Set QR code parameters
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            hints.put(EncodeHintType.MARGIN, 0);

            // Generate QR code
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeData, BarcodeFormat.QR_CODE, qrCodeSize, qrCodeSize, hints);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            BufferedImage qrCodeImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    qrCodeImage.setRGB(x, y, bitMatrix.get(x, y) ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
                }
            }
            return qrCodeImage;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public BufferedImage toBufferedImage(BitMatrix matrix) {

        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int[] pixels = new int[width * height];

        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = matrix.get(x, y) ? BLACK : WHITE;
            }
        }

        image.setRGB(0, 0, width, height, pixels, 0, width);
        return image;
    }

    public String getLocator(String message){
        int length = message.length();
        int middleIndex = length / 2;
        int startIndex = middleIndex - 7;  // 7 characters before the middle
        int endIndex = middleIndex + 8;    // 8 characters after the middle
        return message.substring(startIndex, endIndex);
    }

    public String getRole(String Id){
        Permission permission = permissionRepository.findPermissionById(Id);
        return permission.getRole();
    }

    // Process Loan Disbursemment
    public Pair<Boolean, String> disburseLoan(CustomerLoan customerLoan){
        LoanConfig config = loanConfigRepository.findLoanConfigByType("disburse");
        if(Objects.equals(config, null)){
            return Pair.of(false, "Disbursement account has not been set");
        }

        Customer customer = customerRepository.findCustomerById(customerLoan.getCustomer().getId());
        User user = userRepository.findUserById(customer.getUser().getId());
        Account account = accountRepository.findAccountByCustomerId(customer.getId());
        InternalAccount internalAccount = internalAccountRepository.findInternalAccountById(config.getSourceId());

        // Process and credit customer
        double newCustomerBal = Double.parseDouble(customerLoan.getLoanAmount()) + Double.parseDouble(account.getBalance());
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        String formattedValue = decimalFormat.format(newCustomerBal);
        account.setBalance(formattedValue);
        accountRepository.save(account);

        // Debit internal account
        double newInternalBal = internalAccount.getBal() - Double.parseDouble(customerLoan.getLoanAmount());
        internalAccount.setBal(newInternalBal);
        internalAccountRepository.save(internalAccount);

        // Create Customer Transaction Logs
        createTransactionLog(internalAccount.getAccountNo(), bank_code, account.getAccountNo(), bank_code, customerLoan.getLoanAmount(), "LOAN DISBURSMENT");
        return Pair.of(true, "Disbursed successful");
    }
}
