package com.savitech.fintab.service.impl;

import com.savitech.fintab.entity.Account;
import com.savitech.fintab.entity.Customer;
import com.savitech.fintab.entity.PasswordResetToken;
import com.savitech.fintab.entity.User;
import com.savitech.fintab.entity.impl.Login;
import com.savitech.fintab.entity.impl.ResetPassword;
import com.savitech.fintab.entity.impl.ResetPasswordConfirm;
import com.savitech.fintab.repository.AccountRepository;
import com.savitech.fintab.repository.CustomerRepository;
import com.savitech.fintab.repository.PasswordResetTokenRepository;
import com.savitech.fintab.repository.UserRepository;
import com.savitech.fintab.security.CustomUserDetailsService;
import com.savitech.fintab.service.LoginService;
import com.savitech.fintab.util.EmailNotification;
import com.savitech.fintab.util.JwtTokenUtil;
import com.savitech.fintab.util.RandomStringGenerator;
import com.savitech.fintab.util.Response;
import lombok.SneakyThrows;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;


@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private Response response;

    @Autowired
    private PasswordResetTokenRepository resetTokenRepository;

    @Autowired
    private RandomStringGenerator generator;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailNotification notification;

    @Override
    public ResponseEntity<?> signIn(Login login) {

        if(accountRepository.existsByAccountNo(login.getUsername())){
            Account account = accountRepository.findAccountByAccountNo(login.getUsername());
            Optional<Customer> customer = customerRepository.findById(account.getCustomer().getId());
            Optional<User> user = userRepository.findById(customer.get().getUser().getId());
            final UserDetails userDetails = userDetailsService.loadUserByUsername(user.get().getEmail());
            final String token = jwtTokenUtil.generateToken(userDetails);

            Map<Object, Object> data = new HashMap<>();
            Map<Object, Object> profile = new HashMap<>();
            Map<Object, Object> auth = new HashMap<>();

            try {
                authenticate(user.get().getEmail(), login.getPassword());
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }

            auth.put("accessToken", token);
            profile.put("email", user.get().getEmail());
            profile.put("firstName", customer.get().getFirstName());
            profile.put("lastName", customer.get().getLastName());
            profile.put("middleName", customer.get().getMiddleName());
            data.put("user", auth);
            data.put("profile", profile);

            return ResponseEntity.ok(data);

        }else {

            final UserDetails userDetails = userDetailsService.loadUserByUsername(login.getUsername());
            User user = userRepository.findUserByEmail(userDetails.getUsername());

            Optional<Customer> customer = customerRepository.findCustomerByUserId(user.getId());

            Map<Object, Object> data = new HashMap<>();
            Map<Object, Object> profile = new HashMap<>();
            Map<Object, Object> auth = new HashMap<>();
            final String token = jwtTokenUtil.generateToken(userDetails);

            try {
                authenticate(user.getEmail(), login.getPassword());
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }

            auth.put("accessToken", token);
            profile.put("email", user.getEmail());
            profile.put("firstName", customer.get().getFirstName());
            profile.put("lastName", customer.get().getLastName());
            profile.put("middleName", customer.get().getMiddleName());
            data.put("user", auth);
            data.put("profile", profile);

            return ResponseEntity.ok(data);
        }
    }

    @SneakyThrows
    @Override
    @Transactional
    public ResponseEntity<?> resetPassword(ResetPassword password) {
        PasswordResetToken token = new PasswordResetToken();
        String code = generator.generateCode(6);
        Map<String, Object> data = new HashMap<>();
        if(userRepository.existsByEmail(password.getUsername())){
            User user = userRepository.findUserByEmail(password.getUsername());
            Customer customer = customerRepository.findByUserId(user.getId());
            if(resetTokenRepository.existsByUserId(user.getId())){
                resetTokenRepository.deleteByUserId(user.getId());
            }
            token.setCode(code);
            token.setUser(user);
            token.setReference(generator.generateReference(12).toLowerCase());
            resetTokenRepository.save(token);

            data.put("reference", token.getReference());
            data.put("message", "Your code has been sent to your register email and mobile number");

            System.out.println(code);
            notification.sendCode(customer.getFirstName(), code, user.getEmail());
            return ResponseEntity.ok(data);

        }else if (accountRepository.existsByAccountNo(password.getUsername())){
            Account account = accountRepository.findAccountByAccountNo(password.getUsername());
            Customer customer = customerRepository.findCustomerById(account.getCustomer().getId());
            User user = userRepository.findUserById(customer.getUser().getId());

            if(resetTokenRepository.existsByUserId(user.getId())){
                resetTokenRepository.deleteByUserId(user.getId());
            }

            token.setCode(code);
            token.setReference(generator.generateReference(12).toLowerCase());
            token.setUser(user);

            resetTokenRepository.save(token);
            data.put("reference", token.getReference());
            data.put("message", "Your code has been sent to your register email and mobile number");

            System.out.println(code);

            notification.sendCode(customer.getFirstName(), code, user.getEmail());


            return ResponseEntity.ok(data);
        }else {
            return response.failResponse("Account does not exist", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> confirmPasswordReset(ResetPasswordConfirm passwordConfirm) {
        if(resetTokenRepository.existsByCodeAndReference(passwordConfirm.getCode(), passwordConfirm.getReference())){
            PasswordResetToken passwordResetToken = resetTokenRepository.findPasswordResetTokenByCodeAndReference(passwordConfirm.getCode(), passwordConfirm.getReference());
            if(Objects.equals(passwordConfirm.getNewPassword(), passwordConfirm.getConfirmPassword())) {
                User user = userRepository.findUserById(passwordResetToken.getUser().getId());
                user.setPassword(passwordEncoder.encode(passwordConfirm.getNewPassword()));
                userRepository.save(user);

                // remove the code
                resetTokenRepository.delete(passwordResetToken);

                return response.successResponse("Password reset successfully", HttpStatus.OK);
            }else {
                return response.failResponse("Password mismatch", HttpStatus.BAD_REQUEST);
            }

        }else {
            return response.failResponse("invalid code or reference", HttpStatus.BAD_REQUEST);
        }
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
}
