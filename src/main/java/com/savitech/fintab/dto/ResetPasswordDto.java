package com.savitech.fintab.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResetPasswordDto {

    @NotNull(message = "please enter your email or account number")
    private String username;
}
