package com.savitech.fintab.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PermissionDto {
    @NotBlank(message = "Name Can not be blank")
    private String name;

    @NotBlank(message = "Role Can not be blank")
    private String role;
}
