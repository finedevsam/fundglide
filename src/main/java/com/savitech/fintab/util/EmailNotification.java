package com.savitech.fintab.util;


import com.savitech.fintab.config.EmailConfig;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class EmailNotification {

    @Autowired
    private EmailConfig emailConfig;

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

        double amountValue = Double.parseDouble(amount);
        double balanceValue = Double.parseDouble(balance);

        Map<Object, Object> properties = new HashMap<>();
        properties.put("customerName", customerName);
        properties.put("reference", reference);
        properties.put("date", date);
        properties.put("type", type);
        properties.put("description", description);
        properties.put("amount", String.format("%s %,.2f", "₦", amountValue));
        properties.put("balance", String.format("%s %,.2f", "₦", balanceValue));

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
}
