package com.savitech.fintab.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SetPinDto {

    @NotNull(message = "Please enter pin")
    private String pin;

    @NotNull(message = "Please enter password")
    private String password;
}
