package com.savitech.fintab.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddPermissionToStaffDto {
    @NotBlank(message = "Please select permission")
    private String permission;
}
