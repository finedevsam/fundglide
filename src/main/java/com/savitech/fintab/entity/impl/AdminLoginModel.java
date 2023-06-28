package com.savitech.fintab.entity.impl;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AdminLoginModel {
    @NotBlank(message = "Email can not be empty")
    private String email;

    @NotBlank(message = "Password can not be empty")
    private String password;
}
