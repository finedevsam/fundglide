package com.savitech.fintab.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateStaffDto {
    @NotBlank(message = "Firstname can not be empty")
    private String firstName;

    @NotBlank(message = "Lastname can not be empty")
    private String lastName;

    @NotBlank(message = "email can not be empty")
    private String email;

    @NotBlank(message = "Department can not be empty")
    private String department;

    @NotBlank(message = "permission can not be empty")
    private List<String> permission;
}
