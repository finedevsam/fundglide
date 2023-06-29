package com.savitech.fintab.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.savitech.fintab.entity.AdminUser;
import com.savitech.fintab.entity.Permission;
import com.savitech.fintab.entity.User;
import com.savitech.fintab.entity.impl.AddPermissionToStaff;
import com.savitech.fintab.entity.impl.CreateStaffModel;
import com.savitech.fintab.entity.impl.UpdateStaffModel;
import com.savitech.fintab.repository.AdminUserRepository;
import com.savitech.fintab.repository.DepartmentRepository;
import com.savitech.fintab.repository.PermissionRepository;
import com.savitech.fintab.repository.UserRepository;
import com.savitech.fintab.service.ManageStaffUserService;
import com.savitech.fintab.util.AuthenticatedUser;
import com.savitech.fintab.util.EmailNotification;
import com.savitech.fintab.util.Helper;
import com.savitech.fintab.util.RandomStringGenerator;
import com.savitech.fintab.util.Response;

import lombok.SneakyThrows;

@Service
public class ManageStaffUserServiceImpl implements ManageStaffUserService{
    @Autowired
    private AuthenticatedUser authenticatedUser;
    @Autowired
    private Response response;
    @Autowired
    private Helper helper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private RandomStringGenerator stringGenerator;

    @Autowired
    private EmailNotification emailNotification;

    @Override
    @SneakyThrows
    public ResponseEntity<?> createStaff(CreateStaffModel staffModel) {
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
        
        if(userRepository.existsByEmail(staffModel.getEmail())){
            return response.failResponse("Email has been used", HttpStatus.BAD_REQUEST);
        }
        
        for(String i: staffModel.getPermission()){
            if(!permissionRepository.existsById(i)){
                return response.failResponse(String.format("`%s` is not a valid permission", i), HttpStatus.BAD_REQUEST);
            }
        }

        if(!departmentRepository.existsById(staffModel.getDepartment())){
            return response.failResponse("Invalid department", HttpStatus.BAD_REQUEST);
        }

        String password = stringGenerator.generateKey(12).toLowerCase();

        // Create User Record
        User newUser = new User();
        newUser.setEmail(staffModel.getEmail());
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setIsAdmin(true);

        // Create Profile
        AdminUser newAdminUser = new AdminUser();
        newAdminUser.setFirstName(staffModel.getFirstName());
        newAdminUser.setLastName(staffModel.getLastName());
        newAdminUser.setPermission(staffModel.getPermission());
        newAdminUser.setDepartment(staffModel.getDepartment());
        newAdminUser.setUser(newUser);

        userRepository.save(newUser);
        adminUserRepository.save(newAdminUser);

        // Send email
        emailNotification.staffCreateMail(staffModel.getFirstName(), staffModel.getEmail(), password);
        return response.successResponse("Staff created successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> allStaff(Pageable pageable) {
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
        userRepository.findByIsAdmin(true);
        return ResponseEntity.ok().body(adminUserRepository.findAll(pageable).toList());
    }

    @Override
    public ResponseEntity<?> adminUpdateStaff(String Id, UpdateStaffModel updateStaffModel) {
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

        AdminUser newAdUser = adminUserRepository.findAUserById(Id);
        if(Objects.isNull(newAdUser)){
            return response.failResponse("Invalid Id", HttpStatus.BAD_REQUEST);
        }

        if(!Objects.isNull(updateStaffModel.getFirstName())){
            newAdUser.setFirstName(updateStaffModel.getFirstName());
        }

        if(!Objects.isNull(updateStaffModel.getLastName())){
            newAdUser.setLastName(updateStaffModel.getLastName());
        }

        if(!Objects.isNull(updateStaffModel.getDepartment())){
            if(!departmentRepository.existsById(updateStaffModel.getDepartment())){
                return response.failResponse("Invalid department", HttpStatus.BAD_REQUEST);
            }
            newAdUser.setDepartment(updateStaffModel.getDepartment());
        }
        adminUserRepository.save(newAdUser);
        return response.successResponse("Record updated successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> giveUserPermission(String Id, AddPermissionToStaff permissionToStaff) {
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

        Permission permission = permissionRepository.findPermissionById(permissionToStaff.getPermission());
        if(Objects.isNull(permission)){
            return response.failResponse("Invalid permission Id", HttpStatus.BAD_REQUEST);
        }

        AdminUser newAdUser = adminUserRepository.findAUserById(Id);
        if(Objects.isNull(newAdUser)){
            return response.failResponse("Invalid Id", HttpStatus.BAD_REQUEST);
        }
        List<String> newPermission = newAdUser.getPermission();
        
        if(newPermission.contains(permissionToStaff.getPermission())){
            return response.failResponse("User already have this permission", HttpStatus.OK);
        }
        
        newPermission.add(permissionToStaff.getPermission());

        newAdUser.setPermission(newPermission);
        adminUserRepository.save(newAdUser);
        return response.successResponse(String.format("%s permission has been granted to %s", permission.getName(), newAdUser.getFirstName()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> revokeStaffPermission(String staffId, String permissionId) {
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

        Permission permission = permissionRepository.findPermissionById(permissionId);
        if(Objects.isNull(permission)){
            return response.failResponse("Invalid permission Id", HttpStatus.BAD_REQUEST);
        }

        AdminUser newAdUser = adminUserRepository.findAUserById(staffId);
        if(Objects.isNull(newAdUser)){
            return response.failResponse("Invalid staff Id", HttpStatus.BAD_REQUEST);
        }
        List<String> newPermission = newAdUser.getPermission();
        
        if(!newPermission.contains(permissionId)){
            return response.failResponse("User does not have this permission", HttpStatus.OK);
        }
        
        newPermission.remove(permissionId);

        newAdUser.setPermission(newPermission);
        adminUserRepository.save(newAdUser);
        return response.successResponse(String.format("%s permission has been remove from %s", permission.getName(), newAdUser.getFirstName()), HttpStatus.OK);
    }
    
}
