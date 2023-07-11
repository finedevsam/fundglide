package com.savitech.fintab.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangePasswordDto {

    @NotNull(message = "Please enter old password")
    private String oldPassword;

    @NotNull(message = "please enter new password")
    private String newPassword;

    @NotNull(message = "please confirm password")
    private String confirmPassword;
}
