package com.savitech.fintab.entity.impl;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateStaffModel {
     @NotBlank(message = "Firstname can not be empty")
    private String firstName;

    @NotBlank(message = "Lastname can not be empty")
    private String lastName;

    @NotBlank(message = "Department can not be empty")
    private String department;
}
