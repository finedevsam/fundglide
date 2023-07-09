package com.savitech.fintab.entity.impl;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InternalAccountModel {

    @NotNull(message = "Please enter the account code")
    private String accountCode;
}
