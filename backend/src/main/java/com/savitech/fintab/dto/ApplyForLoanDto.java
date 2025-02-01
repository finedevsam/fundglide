package com.savitech.fintab.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ApplyForLoanDto {
    @NotBlank(message = "Please enter the loan id")
    private String loanId;

    @NotBlank(message = "Please enter amount")
    private String amount;

    @NotBlank(message = "Please enter the payment")
    private String other;
}
