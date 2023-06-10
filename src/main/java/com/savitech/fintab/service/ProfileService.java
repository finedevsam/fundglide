package com.savitech.fintab.service;

import com.savitech.fintab.entity.impl.ChangePassword;
import com.savitech.fintab.entity.impl.UpdateProfile;
import org.springframework.http.ResponseEntity;

public interface ProfileService {

    ResponseEntity<?> updateProfile(UpdateProfile profile);

    ResponseEntity<?> loggedInUser();

    ResponseEntity<?> changePassword(ChangePassword changePassword);

    ResponseEntity<?> activateQRCodePayment();
}
