package com.savitech.fintab.util;

import com.savitech.fintab.entity.Account;
import com.savitech.fintab.repository.AccountRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Component
public class Helper {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private FloatFormat floatFormat;

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
}
