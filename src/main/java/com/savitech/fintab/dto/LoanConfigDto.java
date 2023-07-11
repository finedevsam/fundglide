package com.savitech.fintab.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoanConfigDto {
    @NotBlank(message = "Please enter name")
    private String name;

    @NotBlank(message = "Please enter type")
    private String type;

    @NotBlank(message = "please enter source Id")
    private String sourceId;

}
