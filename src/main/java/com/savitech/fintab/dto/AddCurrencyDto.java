package com.savitech.fintab.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddCurrencyDto {
    @NotNull(message = "Kindly enter the currency name")
    private String currencyName;

    @NotNull(message = "Kindly enter currency code")
    private String code;
}
