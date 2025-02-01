package com.savitech.fintab.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InternalAccountDto {

    @NotNull(message = "Please enter the account code")
    private String accountCode;
}
