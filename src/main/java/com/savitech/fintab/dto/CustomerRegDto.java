package com.savitech.fintab.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CustomerRegDto {

    @NotNull(message = "Enter first Name")
    private String firstName;

    @NotNull(message = "Enter Last Name")
    private String lastName;

    @NotNull(message = "Enter Email")
    private String email;

    @NotNull(message = "Enter Phone Number")
    private String phoneNumber;

    @NotNull(message = "Enter Password")
    private String password;
}
