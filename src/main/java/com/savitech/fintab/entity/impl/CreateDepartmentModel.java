package com.savitech.fintab.entity.impl;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateDepartmentModel {
    @NotBlank(message = "Kindly enter department name")
    private String name;
}
