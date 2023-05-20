package com.savitech.fintab.service.impl;

import com.savitech.fintab.entity.Account;
import com.savitech.fintab.entity.Customer;
import com.savitech.fintab.entity.User;
import com.savitech.fintab.entity.impl.Login;
import com.savitech.fintab.repository.AccountRepository;
import com.savitech.fintab.repository.CustomerRepository;
import com.savitech.fintab.repository.UserRepository;
import com.savitech.fintab.security.CustomUserDetailsService;
import com.savitech.fintab.service.LoginService;
import com.savitech.fintab.util.JwtTokenUtil;
import com.savitech.fintab.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
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
