package com.savitech.fintab.entity.impl;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddPermissionToStaff {
    @NotBlank(message = "Please select permission")
    private String permission;
}
