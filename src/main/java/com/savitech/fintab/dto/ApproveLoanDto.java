package com.savitech.fintab.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ApproveLoanDto {

    @NotBlank(message = "Please enter the source account")
    private String source;
}
