package com.savitech.fintab.entity.impl;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PermissionModel {
    @NotBlank(message = "Name Can not be blank")
    private String name;

    @NotBlank(message = "Role Can not be blank")
    private String role;
}
