package com.savitech.fintab.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResetPasswordConfirm {

    @NotNull(message = "please enter reference")
    private String reference;

    @NotNull(message = "please enter code")
    private String code;

    @NotNull(message = "please enter new password")
    private String newPassword;

    @NotNull(message = "confirm new password")
    private String confirmPassword;
}
