package com.savitech.fintab.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.savitech.fintab.entity.AdminUser;
import com.savitech.fintab.entity.LoanType;
import com.savitech.fintab.entity.User;
import com.savitech.fintab.entity.impl.LoanTypeModel;
import com.savitech.fintab.repository.AdminUserRepository;
import com.savitech.fintab.repository.LoanTypeRepository;
import com.savitech.fintab.service.LoanService;
import com.savitech.fintab.util.AuthenticatedUser;
import com.savitech.fintab.util.Helper;
import com.savitech.fintab.util.RandomStringGenerator;
import com.savitech.fintab.util.Response;

@Service
public class LoanServiceImpl implements LoanService{

    @Autowired
    private AuthenticatedUser authenticatedUser;

    @Autowired
    private LoanTypeRepository loanTypeRepository;

    @Autowired
    private Response response;

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private RandomStringGenerator stringGenerator;

    @Autowired
    private Helper helper;

    @Override
    public ResponseEntity<?> createLoanType(LoanTypeModel loanTypeModel) {
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

        if(
            Objects.equals(loanTypeModel.getName(), null) || 
            Objects.equals(loanTypeModel.getName(), "") || 
            Objects.equals(loanTypeModel.getRate(), null) ||
            Objects.equals(loanTypeModel.getRate(), "") ||
            Objects.equals(loanTypeModel.getTenure(), null) ||
            Objects.equals(loanTypeModel.getTenure(), "")
        ){
            return response.failResponse("loan name, rate or tenure can't be empty", HttpStatus.BAD_REQUEST);
        }

        LoanType loanType = new LoanType();
        loanType.setCode(stringGenerator.generateReference(7).toUpperCase());
        loanType.setName(loanTypeModel.getName());
        loanType.setRate(loanTypeModel.getRate());
        loanType.setTenure(loanTypeModel.getTenure());
        loanTypeRepository.save(loanType);

        return response.successResponse("Loan type created", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> allLoan(Pageable pageable) {
        return ResponseEntity.ok().body(loanTypeRepository.findAll(pageable).toList());
    }

    @Override
    public ResponseEntity<?> updateLoanType(String Id, LoanTypeModel loanTypeModel) {
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

        if(!loanTypeRepository.existsById(Id)){
            return response.failResponse("Invalid loan type id", HttpStatus.BAD_REQUEST);
        }

        LoanType loanType = loanTypeRepository.findLoanTypeById(Id);

        if(!Objects.equals(loanTypeModel.getName(), null) || !Objects.equals(loanTypeModel.getName(), "")){
            loanType.setName(loanTypeModel.getName());
        }

        if(!Objects.equals(loanTypeModel.getRate(), null) || !Objects.equals(loanTypeModel.getRate(), "")){
            loanType.setRate(loanTypeModel.getRate());
        }

        if(!Objects.equals(loanTypeModel.getTenure(), null) || !Objects.equals(loanTypeModel.getTenure(), "")){
            loanType.setTenure(loanTypeModel.getTenure());
        }
        loanTypeRepository.save(loanType);
        return response.successResponse("Loan type updated successfully", HttpStatus.OK);
    }
    
}
