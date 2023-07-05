package com.savitech.fintab.entity.impl;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoanTypeModel {
    @NotBlank(message = "please enter name")
    private String name;

    @NotBlank(message = "Please enter tenure")
    private String tenure;

    @NotBlank(message = "please enter rate")
    private String rate;

    @NotBlank(message = "please enter code")
    private String code;
}
