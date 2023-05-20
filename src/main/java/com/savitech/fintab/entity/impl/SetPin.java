package com.savitech.fintab.entity.impl;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SetPin {

    @NotNull(message = "Please enter pin")
    private String pin;

    @NotNull(message = "Please enter password")
    private String password;
}
