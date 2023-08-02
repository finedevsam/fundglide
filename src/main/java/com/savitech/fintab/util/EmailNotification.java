package com.savitech.fintab.util;


import com.savitech.fintab.config.EmailConfig;
import com.savitech.fintab.entity.CurrencyConfig;
import com.savitech.fintab.repository.CurrencyConfigRepository;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class EmailNotification {

    @Autowired
    private EmailConfig emailConfig;

    @Autowired
    private CurrencyConfigRepository currencyConfigRepository;

    public void registrationEmail(String customerName,
                                  String account_no,
                                  String customerFullName,
                                  String customerEmail,
                                  String customerPhoneNo
    ) throws MessagingException {
        Map<Object, Object> properties = new HashMap<>();
        properties.put("customerName", customerName);
        properties.put("account_no", account_no);
        properties.put("customerFullName", customerFullName);
        properties.put("customerEmail", customerEmail);
        properties.put("customerPhoneNo", customerPhoneNo);

        emailConfig.sendHtmlMessage(properties, customerEmail, "Welcome", "register.html");
    }


    public void sendReceipt(
            String customerName,
            String reference,
            Date date, String type,
            String description,
            String amount,
            String email,
            String balance
            ) throws MessagingException {
        
        

        String currency = null;

        CurrencyConfig currencyConfig = currencyConfigRepository.findCurrencyConfigByIsDefault(true);

        if(Objects.equals(currencyConfig, null)){
            currency = "USD";
        }else{
            currency = currencyConfig.getCode();
        }
        double amountValue = Double.parseDouble(amount);
        double balanceValue = Double.parseDouble(balance);

        Map<Object, Object> properties = new HashMap<>();
        properties.put("customerName", customerName);
        properties.put("reference", reference);
        properties.put("date", date);
        properties.put("type", type);
        properties.put("description", description);
        properties.put("amount", String.format("%s %,.2f", currency, amountValue));
        properties.put("balance", String.format("%s %,.2f", currency, balanceValue));

        emailConfig.sendHtmlMessage(properties, email, "Transaction Receipt", "receipt.html");
    }

    public void sendCode(String name, String code, String email) throws MessagingException {
        Map<Object, Object> properties = new HashMap<>();
        properties.put("name", name);
        properties.put("code", code);
        emailConfig.sendHtmlMessage(properties, email, "One Time Password", "otp.html");
    }

    public void changePasswordMail(String name, String email) throws MessagingException{
        Map<Object, Object> properties = new HashMap<>();
        properties.put("name", name);
        emailConfig.sendHtmlMessage(properties, email, "Change Password", "changepass.html");
    }

    public void staffCreateMail(String staffName, String email, String password) throws MessagingException{
        Map<Object, Object> properties = new HashMap<>();
        properties.put("staffName", staffName);
        properties.put("password", password);
        properties.put("email", email);
        emailConfig.sendHtmlMessage(properties, email, "Staff Onboarding", "staffregister.html");
    }

    public void AutoSaveEmail(String customerName, String email, double amount, String loanTitle) throws MessagingException{
        Map<Object, Object> properties = new HashMap<>();

        String currency = null;

        CurrencyConfig currencyConfig = currencyConfigRepository.findCurrencyConfigByIsDefault(true);

        if(Objects.equals(currencyConfig, null)){
            currency = "USD";
        }else{
            currency = currencyConfig.getCode();
        }

        properties.put("customerName", customerName);
        properties.put("amount", amount);
        properties.put("email", email);
        properties.put("currency", currency);
        properties.put("loanTitle", loanTitle.toUpperCase());
        emailConfig.sendHtmlMessage(properties, email, "Staff Onboarding", "auto_save.html");
    }
}
