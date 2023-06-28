package com.savitech.fintab.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.savitech.fintab.entity.AdminUser;
import com.savitech.fintab.entity.User;
import com.savitech.fintab.entity.impl.AdminLoginModel;
import com.savitech.fintab.repository.AdminUserRepository;
import com.savitech.fintab.repository.UserRepository;
import com.savitech.fintab.security.CustomUserDetailsService;
import com.savitech.fintab.service.AdminLoginService;
import com.savitech.fintab.util.AuthenticatedUser;
import com.savitech.fintab.util.JwtTokenUtil;
import com.savitech.fintab.util.Response;

@Service
public class AdminLoginServiceImpl implements AdminLoginService{

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private Response response;

    @Autowired
    private AuthenticatedUser authenticatedUser;

    @Override
    public ResponseEntity<?> login(AdminLoginModel adminLoginModel) {
        final UserDetails userDetails = userDetailsService.loadUserByUsername(adminLoginModel.getEmail());
        User user = userRepository.findUserByEmail(userDetails.getUsername());

        if(!user.getIsAdmin()){
            return response.failResponse("Permission Denied", HttpStatus.BAD_GATEWAY);
        }

        AdminUser adminUser = adminUserRepository.findByUserId(user.getId());
        Map<Object, Object> data = new HashMap<>();
        Map<Object, Object> profile = new HashMap<>();
        Map<Object, Object> auth = new HashMap<>();
        final String token = jwtTokenUtil.generateToken(userDetails);

        try {
            authenticate(user.getEmail(), adminLoginModel.getPassword());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        auth.put("accessToken", token);
        profile.put("email", user.getEmail());
        profile.put("firstName", adminUser.getFirstName());
        profile.put("lastName", adminUser.getLastName());
        profile.put("department", adminUser.getDepartment());
        profile.put("imageUrl", adminUser.getProfileImage());
        profile.put("permission", adminUser.getPermission());
        data.put("user", auth);
        data.put("profile", profile);

        return ResponseEntity.ok(data);
    }
    
    private void authenticate(String email, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    email, password));

        } catch (DisabledException e){
            throw new Exception("User disabled");
        } catch (BadCredentialsException e){
            throw new Exception("Bad Credentials");
        }
    }

    @Override
    public ResponseEntity<?> loggedInAdmin() {
        User user = authenticatedUser.auth();
        AdminUser adminUser = adminUserRepository.findByUserId(user.getId());

        Map<Object, Object> data = new HashMap<>();
        Map<Object, Object> profile = new HashMap<>();
        profile.put("email", user.getEmail());
        profile.put("firstName", adminUser.getFirstName());
        profile.put("lastName", adminUser.getLastName());
        profile.put("imageUrl", adminUser.getProfileImage());
        profile.put("department", adminUser.getDepartment());
        profile.put("permission", adminUser.getPermission());
        data.put("profile", profile);

        return ResponseEntity.ok(data);
    }
}
