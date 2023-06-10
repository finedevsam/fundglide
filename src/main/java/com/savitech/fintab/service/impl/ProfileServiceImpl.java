package com.savitech.fintab.service.impl;

import com.savitech.fintab.entity.Account;
import com.savitech.fintab.entity.Customer;
import com.savitech.fintab.entity.User;
import com.savitech.fintab.entity.impl.ChangePassword;
import com.savitech.fintab.entity.impl.UpdateProfile;
import com.savitech.fintab.repository.AccountRepository;
import com.savitech.fintab.repository.CustomerRepository;
import com.savitech.fintab.repository.UserRepository;
import com.savitech.fintab.service.ProfileService;
import com.savitech.fintab.util.AuthenticatedUser;
import com.savitech.fintab.util.EmailNotification;
import com.savitech.fintab.util.Response;
import com.savitech.fintab.util.UploadFile;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;

@Service
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    private UploadFile uploadFile;

    @Autowired
    private Response response;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AuthenticatedUser authenticatedUser;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailNotification notification;

    @Override
    public ResponseEntity<?> updateProfile(UpdateProfile profile) {
        User user = authenticatedUser.auth();
        Customer customer = customerRepository.findByUserId(user.getId());

        List<String> acceptedIDType = new ArrayList<>();
        acceptedIDType.add("passport");
        acceptedIDType.add("driver");
        acceptedIDType.add("voter");

        if (profile.getModeOfId() != null && !acceptedIDType.contains(profile.getModeOfId())){
            return response.failResponse(String.format("%s is the accepted mode of ID", acceptedIDType), HttpStatus.BAD_REQUEST);
        }

        if(!profile.getIdData().isEmpty() && profile.getModeOfId() == null){
            return response.failResponse("Please select mode of ID", HttpStatus.BAD_REQUEST);
        }

        if(profile.getAddress() != null){
            if (profile.getAddress().equals("")){
                customer.setAddress(customer.getAddress());
            }else {
                customer.setAddress(profile.getAddress());
            }
        } else {
            customer.setAddress(customer.getAddress());
        }

        if(profile.getFirstName() != null){
            if (profile.getFirstName().equals("")) {
                customer.setFirstName(customer.getFirstName());
            } else {
                customer.setFirstName(profile.getFirstName());
            }
        } else {
            customer.setFirstName(customer.getFirstName());
        }

        if(profile.getLastName() != null){
            if (profile.getLastName().equals("")) {
                customer.setLastName(customer.getLastName());
            } else {
                customer.setLastName(profile.getLastName());
            }
        } else {
            customer.setLastName(customer.getLastName());
        }

        if(profile.getMiddleName() != null){
            if(profile.getMiddleName().equals("")){
                customer.setMiddleName(customer.getMiddleName());
            } else {
                customer.setMiddleName(profile.getMiddleName());
            }
        } else {
            customer.setMiddleName(customer.getMiddleName());
        }

        if(profile.getIdData() != null){
            if(profile.getIdData().isEmpty()){
                customer.setIdData(customer.getIdData());
            }else {
                Pair<Boolean, String> files = uploadFile.uploadCloudinary(profile.getIdData());
                if(files.getFirst()){
                    customer.setIdData(files.getSecond());
                }else {
                    customer.setIdData(customer.getIdData());
                }
            }
        } else {
            customer.setIdData(customer.getIdData());
        }

        if (profile.getModeOfId() != null){
            if (profile.getModeOfId().equals("")){
                customer.setModeOfId(customer.getModeOfId());
            } else {
                customer.setModeOfId(profile.getModeOfId());
            }
        }else {
            customer.setModeOfId(customer.getModeOfId());
        }

        if(profile.getProfileImage() != null) {
            if (profile.getProfileImage().isEmpty()){
                customer.setProfileImage(customer.getProfileImage());
            } else {
                Pair<Boolean, String> files = uploadFile.uploadCloudinary(profile.getProfileImage());
                if(files.getFirst()){
                    customer.setProfileImage(files.getSecond());
                } else {
                    customer.setProfileImage(customer.getProfileImage());
                }
            }
        }else {
            customer.setProfileImage(customer.getProfileImage());
        }

        if(profile.getMobileNumber() != null){
            if(profile.getMobileNumber().equals("")){
                customer.setPhoneNumber(customer.getPhoneNumber());
            }else {
                customer.setPhoneNumber(profile.getMobileNumber());
            }
        }else {
            customer.setPhoneNumber(customer.getPhoneNumber());
        }
        customerRepository.save(customer);
        return response.successResponse("Profile updated successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> loggedInUser() {
        User user = authenticatedUser.auth();
        Optional<Customer> customer = customerRepository.findCustomerByUserId(user.getId());
        Account account = accountRepository.findAccountByCustomerId(customer.get().getId());

        Map<Object, Object> data = new HashMap<>();
        data.put("firstName", customer.get().getFirstName());
        data.put("email", user.getEmail());
        data.put("isCustomer", user.getIsCustomer());
        data.put("lastName", customer.get().getLastName());
        data.put("middleName", customer.get().getMiddleName());
        data.put("modeOfId", customer.get().getModeOfId());
        data.put("idData", customer.get().getIdData());
        data.put("profileImage", customer.get().getProfileImage());
        data.put("address", customer.get().getAddress());
        data.put("accounts", account);
        data.put("dateRegistered", user.getCreatedAt());
        return ResponseEntity.ok(data);
    }

    @SneakyThrows
    @Override
    public ResponseEntity<?> changePassword(ChangePassword changePassword) {
        User user = authenticatedUser.auth();
        Customer customer = customerRepository.findByUserId(user.getId());
        if (!ObjectUtils.isEmpty(changePassword)){
            if(Objects.equals(changePassword.getNewPassword(), changePassword.getConfirmPassword())) {
                if (passwordEncoder.matches(changePassword.getOldPassword(), user.getPassword())) {
                    user.setPassword(passwordEncoder.encode(changePassword.getNewPassword()));
                    userRepository.save(user);

                    // Send Notification about change password
                    notification.changePasswordMail(customer.getFirstName(), user.getEmail());
                    return response.successResponse("password change successfully", HttpStatus.OK);
                }else {
                    return response.failResponse("invalid password", HttpStatus.BAD_REQUEST);
                }
            }else {
                return response.failResponse("password mismatch", HttpStatus.BAD_REQUEST);
            }
        }else {
            return response.failResponse("kindly include all the require body", HttpStatus.BAD_REQUEST);
        }
    }
}
