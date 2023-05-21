package com.savitech.fintab.entity.impl;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateProfile {

    private String address;

    private String firstName;

    private String lastName;

    private String middleName;

    private MultipartFile idData;

    private String modeOfId;

    private String mobileNumber;

    private MultipartFile profileImage;
}
