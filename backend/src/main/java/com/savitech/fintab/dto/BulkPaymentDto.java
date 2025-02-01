package com.savitech.fintab.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


@Data
public class BulkPaymentDto {
//    private String mode;

    private MultipartFile file;

    private String paymentType;

    private String debitedAccount;

    private String transactionPin;

    private String description;

    private String date;
}
