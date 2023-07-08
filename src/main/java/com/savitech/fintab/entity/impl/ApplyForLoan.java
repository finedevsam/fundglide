package com.savitech.fintab.entity.impl;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ApplyForLoan {
    @NotBlank(message = "Please enter the loan id")
    private String loanId;

    @NotBlank(message = "Please enter amount")
    private String amount;

    @NotBlank(message = "Please enter the payment")
    private String other;
}
