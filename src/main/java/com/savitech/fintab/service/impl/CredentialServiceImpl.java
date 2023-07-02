package com.savitech.fintab.service.impl;

import com.savitech.fintab.entity.Credential;
import com.savitech.fintab.entity.User;
import com.savitech.fintab.entity.impl.SetPin;
import com.savitech.fintab.repository.CredentialRepository;
import com.savitech.fintab.service.CredentialService;
import com.savitech.fintab.util.AuthenticatedUser;
import com.savitech.fintab.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CredentialServiceImpl implements CredentialService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CredentialRepository credentialRepository;

    @Autowired
    private Response response;

    @Autowired
    private AuthenticatedUser authenticatedUser;

    @Override
    public ResponseEntity<?> setTransactionPin(SetPin pin) {

        // Get Authenticated User
        User user = authenticatedUser.auth();

        Credential credential = credentialRepository.findByUserId(user.getId());

        boolean isPinNumeric = pin.getPin().matches("\\d+");
        if (!isPinNumeric){
            return response.failResponse("Transaction pin must be numeric", HttpStatus.BAD_REQUEST);
        }

        if(pin.getPin().length() != 4){
            return response.failResponse("Transaction pin must be 4", HttpStatus.BAD_REQUEST);
        }
        boolean isPasswordValid = passwordEncoder.matches(pin.getPassword(), user.getPassword());

        if(isPasswordValid) {
            if (credential != null) {
                credential.setPin(passwordEncoder.encode(pin.getPin()));
                credentialRepository.save(credential);
            }else {
                Credential newCredential = new Credential();
                newCredential.setPin(passwordEncoder.encode(pin.getPin()));
                newCredential.setUser(user);
                credentialRepository.save(newCredential);
            }
            return response.successResponse("Transaction Pin set successfully", HttpStatus.OK);
        }else {
            return response.failResponse("Wrong password", HttpStatus.BAD_REQUEST);
        }
    }
}
