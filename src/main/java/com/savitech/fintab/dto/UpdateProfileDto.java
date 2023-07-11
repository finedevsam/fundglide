package com.savitech.fintab.dto;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateProfileDto {

    private String address;

    private String firstName;

    private String lastName;

    private String middleName;

    private MultipartFile idData;

    private String modeOfId;

    private String mobileNumber;

    private MultipartFile profileImage;
}
