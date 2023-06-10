package com.savitech.fintab.entity.impl;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangePassword {

    @NotNull(message = "Please enter old password")
    private String oldPassword;

    @NotNull(message = "please enter new password")
    private String newPassword;

    @NotNull(message = "please confirm password")
    private String confirmPassword;
}
