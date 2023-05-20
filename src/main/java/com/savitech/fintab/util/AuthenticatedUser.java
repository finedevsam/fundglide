package com.savitech.fintab.util;

import com.savitech.fintab.entity.User;
import com.savitech.fintab.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticatedUser {

    @Autowired
    private UserRepository userRepository;

    public User auth(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findUserByEmail(authentication.getName());
    }
}
