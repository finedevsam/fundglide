package com.savitech.fintab.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginDto {

    @NotNull(message = "Enter User name")
    private String username;

    @NotNull(message = "Enter Password")
    private String password;
}
