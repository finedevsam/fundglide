package com.savitech.fintab.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateDepartmentDto {
    @NotBlank(message = "Kindly enter department name")
    private String name;
}
