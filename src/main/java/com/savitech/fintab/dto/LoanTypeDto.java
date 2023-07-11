package com.savitech.fintab.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoanTypeDto {
    @NotBlank(message = "please enter name")
    private String name;

    @NotBlank(message = "Please enter tenure")
    private String tenure;

    @NotBlank(message = "please enter rate")
    private String rate;

    @NotBlank(message = "please enter code")
    private String code;
}
