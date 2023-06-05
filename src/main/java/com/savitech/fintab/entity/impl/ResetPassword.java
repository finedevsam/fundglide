package com.savitech.fintab.entity.impl;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResetPassword {

    @NotNull(message = "please enter your email or account number")
    private String username;
}
