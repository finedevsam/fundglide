package com.savitech.fintab.entity.impl;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Login {

    @NotNull(message = "Enter User name")
    private String username;

    @NotNull(message = "Enter Password")
    private String password;
}
