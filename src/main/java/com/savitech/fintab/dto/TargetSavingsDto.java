package com.savitech.fintab.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TargetSavingsDto {

    @NotNull(message = "Title can not be null")
    @NotBlank(message = "Title can not be blank")
    private String title;

    @NotNull(message = "Target amount can not be null")
    @NotBlank(message = "Target amount can not be blank")
    private double targetAmount;
}
