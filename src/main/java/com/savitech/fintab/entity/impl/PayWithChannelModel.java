package com.savitech.fintab.entity.impl;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PayWithChannelModel {

    @NotNull(message = "please enter correct encrypted data")
    private String data;

    @NotNull(message = "please select channel")
    private String channel;
}
