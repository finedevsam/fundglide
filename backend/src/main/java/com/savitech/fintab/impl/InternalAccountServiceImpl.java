package com.savitech.fintab.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.savitech.fintab.dto.InternalAccountDto;
import com.savitech.fintab.entity.AdminUser;
import com.savitech.fintab.entity.InternalAccount;
import com.savitech.fintab.entity.User;
import com.savitech.fintab.repository.AdminUserRepository;
import com.savitech.fintab.repository.InternalAccountRepository;
import com.savitech.fintab.service.InternalAccountService;
import com.savitech.fintab.util.AuthenticatedUser;
import com.savitech.fintab.util.GenerateAccountNumber;
import com.savitech.fintab.util.Helper;
import com.savitech.fintab.util.Response;

@Service
public class InternalAccountServiceImpl implements InternalAccountService {

    @Autowired
    private AuthenticatedUser authenticatedUser;

    @Autowired
    private Response response;

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private Helper helper;

    @Autowired
    private InternalAccountRepository internalAccountRepository;

    @Autowired
    private GenerateAccountNumber generateAccountNumber;

    @Override
    public ResponseEntity<?> createInternalAccount(InternalAccountDto internalAccountModel) {
        User user = authenticatedUser.auth();
        if(!user.getIsAdmin()){
            return response.failResponse("You don't have permission to perform this opeation", HttpStatus.BAD_REQUEST);
        }
        AdminUser adminUser = adminUserRepository.findByUserId(user.getId());

        if(adminUser.getPermission().size() < 1){
            return response.failResponse("You don't have permission to perform this opeation", HttpStatus.BAD_REQUEST);
        }

        List<String> createPermissionlist = Arrays.asList("all");
        
        List<String> myPermission = adminUser.getPermission();
        for(String id: myPermission){
            if(!createPermissionlist.contains(helper.getRole(id))){
                return response.failResponse("You don't have permission to perform this opeation", HttpStatus.BAD_REQUEST);
            }
        }

        if(internalAccountModel.getAccountCode().length() > 10){
            return response.failResponse("Account code can't be more than 10 character length", HttpStatus.BAD_REQUEST);
        }

        if(Objects.equals(internalAccountModel.getAccountCode(), null) || Objects.equals(internalAccountModel.getAccountCode(), "")){
            return response.failResponse("Account code can not be null or empty", HttpStatus.BAD_REQUEST);
        }

        String accountCode = String.format("GL-%s", internalAccountModel.getAccountCode().toUpperCase());

        if(internalAccountRepository.existsByAccountCode(accountCode)){
            return response.failResponse("Account code already exist", HttpStatus.BAD_REQUEST);
        }

        InternalAccount internalAccount = new InternalAccount();
        internalAccount.setAccountCode(accountCode);
        internalAccount.setAccountNo(generateAccountNumber.internalAccountNumber(10));
        internalAccountRepository.save(internalAccount);
        return response.successResponse("Internal account created successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> allInternalAccount(Pageable pageable) {
        User user = authenticatedUser.auth();
        if(!user.getIsAdmin()){
            return response.failResponse("You don't have permission to perform this opeation", HttpStatus.BAD_REQUEST);
        }
        AdminUser adminUser = adminUserRepository.findByUserId(user.getId());

        if(adminUser.getPermission().size() < 1){
            return response.failResponse("You don't have permission to perform this opeation", HttpStatus.BAD_REQUEST);
        }

        List<String> createPermissionlist = Arrays.asList("all");
        
        List<String> myPermission = adminUser.getPermission();
        for(String id: myPermission){
            if(!createPermissionlist.contains(helper.getRole(id))){
                return response.failResponse("You don't have permission to perform this opeation", HttpStatus.BAD_REQUEST);
            }
        }

        return ResponseEntity.ok().body(internalAccountRepository.findAll(pageable).toList());
    }
    
}
