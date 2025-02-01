package com.savitech.fintab.service;

import org.springframework.http.ResponseEntity;

import com.savitech.fintab.dto.ChangePasswordDto;
import com.savitech.fintab.dto.UpdateProfileDto;

public interface ProfileService {

    ResponseEntity<?> updateProfile(UpdateProfileDto profile);

    ResponseEntity<?> loggedInUser();

    ResponseEntity<?> changePassword(ChangePasswordDto changePassword);

    ResponseEntity<?> activateQRCodePayment();
}
