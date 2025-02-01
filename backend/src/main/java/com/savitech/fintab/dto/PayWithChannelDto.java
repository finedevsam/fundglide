package com.savitech.fintab.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PayWithChannelDto {

    @NotNull(message = "please enter correct encrypted data")
    private String data;

    @NotNull(message = "please select channel")
    private String channel;
}
