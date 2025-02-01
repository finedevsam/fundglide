package com.savitech.fintab.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TransferDto {

    @NotNull(message = "Please enter debit account")
    private String debitAccount;

    @NotNull(message = "Please enter credit account")
    private String creditAccount;

    @NotNull(message = "Please enter amount")
    private String amount;

    @NotNull(message = "Please enter destination bank code")
    private String destinationBankCode;

    @NotNull(message = "Please enter description")
    private String description;

    @NotNull(message = "Please enter transaction pin")
    private String pin;
}
