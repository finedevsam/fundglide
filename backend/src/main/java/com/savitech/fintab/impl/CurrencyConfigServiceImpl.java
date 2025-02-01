package com.savitech.fintab.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.savitech.fintab.dto.AddCurrencyDto;
import com.savitech.fintab.entity.AdminUser;
import com.savitech.fintab.entity.CurrencyConfig;
import com.savitech.fintab.entity.User;
import com.savitech.fintab.repository.AdminUserRepository;
import com.savitech.fintab.repository.CurrencyConfigRepository;
import com.savitech.fintab.service.CurrencyConfigService;
import com.savitech.fintab.util.AuthenticatedUser;
import com.savitech.fintab.util.Helper;
import com.savitech.fintab.util.Response;

@Service
public class CurrencyConfigServiceImpl implements CurrencyConfigService {

    @Autowired
    private AuthenticatedUser authenticatedUser;

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    Response response;

    @Autowired
    Helper helper;

    @Autowired
    CurrencyConfigRepository currencyConfigRepository;

    @Override
    public ResponseEntity<?> addCurrency(AddCurrencyDto addCurrencyDto) {
        User user = authenticatedUser.auth();
        AdminUser adminUser = adminUserRepository.findByUserId(user.getId());

        if(!user.getIsAdmin()){
            return response.failResponse("You don't have permission to perform this opeation", HttpStatus.BAD_REQUEST);
        }

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

        if(currencyConfigRepository.existsByCode(addCurrencyDto.getCode().toUpperCase())){
            return response.failResponse("Currency code already exist", HttpStatus.BAD_REQUEST);
        }

        CurrencyConfig currencyConfig = new CurrencyConfig();
        currencyConfig.setCode(addCurrencyDto.getCode().toUpperCase());
        currencyConfig.setCurrency(addCurrencyDto.getCurrencyName());
        currencyConfigRepository.save(currencyConfig);
        return response.successResponse("Currency Added", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> allCurrency(Pageable pageable) {
        User user = authenticatedUser.auth();
        AdminUser adminUser = adminUserRepository.findByUserId(user.getId());

        if(!user.getIsAdmin()){
            return response.failResponse("You don't have permission to perform this opeation", HttpStatus.BAD_REQUEST);
        }

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

        return ResponseEntity.ok().body(currencyConfigRepository.findAll(pageable).toList());
    }

    @Override
    public ResponseEntity<?> setDefaultCurrency(String currId) {
        User user = authenticatedUser.auth();
        AdminUser adminUser = adminUserRepository.findByUserId(user.getId());

        if(!user.getIsAdmin()){
            return response.failResponse("You don't have permission to perform this opeation", HttpStatus.BAD_REQUEST);
        }

        if(adminUser.getPermission().size() < 1){
            return response.failResponse("You don't have permission to perform this opeation", HttpStatus.BAD_REQUEST);
        }

        List<String> createPermissionlist = Arrays.asList( "all");
        
        List<String> myPermission = adminUser.getPermission();
        for(String id: myPermission){
            if(!createPermissionlist.contains(helper.getRole(id))){
                return response.failResponse("You don't have permission to perform this opeation", HttpStatus.BAD_REQUEST);
            }
        }


        CurrencyConfig newDefault = currencyConfigRepository.findCurrencyConfigById(currId);
        if(Objects.equals(newDefault, null)){
            return response.failResponse("Invalid currency Id", HttpStatus.BAD_REQUEST);
        }
        if(newDefault.getIsDefault()){
            return response.failResponse(String.format("%s is currently the default system currency", newDefault.getCurrency()), HttpStatus.BAD_REQUEST);
        }else{

            CurrencyConfig isDefault = currencyConfigRepository.findCurrencyConfigByIsDefault(true);
            isDefault.setIsDefault(false);;
            currencyConfigRepository.save(isDefault);

            newDefault.setIsDefault(true);
            currencyConfigRepository.save(newDefault);
            System.out.println(newDefault);
            return response.successResponse(String.format("%s set as System default Currency", newDefault.getCurrency()), HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<?> removeCurrency(String currId) {
       User user = authenticatedUser.auth();
        AdminUser adminUser = adminUserRepository.findByUserId(user.getId());

        if(!user.getIsAdmin()){
            return response.failResponse("You don't have permission to perform this opeation", HttpStatus.BAD_REQUEST);
        }

        if(adminUser.getPermission().size() < 1){
            return response.failResponse("You don't have permission to perform this opeation", HttpStatus.BAD_REQUEST);
        }

        List<String> createPermissionlist = Arrays.asList( "all");
        
        List<String> myPermission = adminUser.getPermission();
        for(String id: myPermission){
            if(!createPermissionlist.contains(helper.getRole(id))){
                return response.failResponse("You don't have permission to perform this opeation", HttpStatus.BAD_REQUEST);
            }
        }


        CurrencyConfig newDefault = currencyConfigRepository.findCurrencyConfigById(currId);
        if(Objects.equals(newDefault, null)){
            return response.failResponse("Invalid currency Id", HttpStatus.BAD_REQUEST);
        }

        if(newDefault.getIsDefault()){
            return response.failResponse("You can't delete default currency", HttpStatus.OK);
        }
        currencyConfigRepository.delete(newDefault);
        return response.successResponse("Currency Removed", HttpStatus.OK);
    }
    
}
