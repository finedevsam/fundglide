package com.savitech.fintab.dto;

import java.time.LocalTime;

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

    @NotNull(message = "Auto Save day can not be null")
    @NotBlank(message = "Auto Save day can not be blank")
    private String autoSaveDay;

    @NotNull(message = "Auto Save Time can not be null")
    @NotBlank(message = "Auto Save Time can not be blank")
    private LocalTime autoSaveTime;

    @NotNull(message = "Auto Save Amount can not be null")
    @NotBlank(message = "Auto Save Amount can not be blank")
    private double autoSavingsAmount;

    @NotNull(message = "Auto Save Type can not be null")
    @NotBlank(message = "Auto Save Type can not be blank")
    private String autoSaveType;

}
