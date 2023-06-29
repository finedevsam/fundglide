package com.savitech.fintab.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.savitech.fintab.entity.AdminUser;
import com.savitech.fintab.entity.Permission;
import com.savitech.fintab.entity.User;
import com.savitech.fintab.entity.impl.PermissionModel;
import com.savitech.fintab.entity.impl.PermissionUpdateModel;
import com.savitech.fintab.repository.AdminUserRepository;
import com.savitech.fintab.repository.PermissionRepository;
import com.savitech.fintab.service.PermissionManagerService;
import com.savitech.fintab.util.AuthenticatedUser;
import com.savitech.fintab.util.Helper;
import com.savitech.fintab.util.Response;

@Service
public class PermissionManagerServiceImpl implements PermissionManagerService{

    @Autowired
    private AuthenticatedUser authenticatedUser;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private Response response;

    @Autowired
    private Helper helper;

    @Override
    public ResponseEntity<?> createRole(PermissionModel permissionModel) {
        User user = authenticatedUser.auth();
        AdminUser adminUser = adminUserRepository.findByUserId(user.getId());

        if(!user.getIsAdmin()){
            return response.failResponse("You don't have permission to perform this opeation", HttpStatus.BAD_REQUEST);
        }

        if(adminUser.getPermission().size() < 1){
            return response.failResponse("You don't have permission to perform this opeation", HttpStatus.BAD_REQUEST);
        }

        if(permissionRepository.existsByRole(permissionModel.getRole())){
            return response.failResponse("Permission already created", HttpStatus.BAD_REQUEST);
        }
        List<String> createPermissionlist = Arrays.asList("create", "all");
        
        List<String> myPermission = adminUser.getPermission();
        for(String id: myPermission){
            if(!createPermissionlist.contains(helper.getRole(id))){
                return response.failResponse("You don't have permission to perform this opeation", HttpStatus.BAD_REQUEST);
            }
        }

        if(!checkRoleType(permissionModel.getRole())){
            return response.failResponse("Invalid accepted role type", HttpStatus.BAD_REQUEST);
        }

        
        Permission newPermission = new Permission();
        newPermission.setName(permissionModel.getName());
        newPermission.setRole(permissionModel.getRole());
        permissionRepository.save(newPermission);
        return response.successResponse("Role created successfully", HttpStatus.OK);
    }
    
    

    private Boolean checkRoleType(String role){
        List<String> list = Arrays.asList("create", "delete", "update", "approval");
        if(list.contains(role)){
            return true;
        }
        return false;
    }

    @Override
    public ResponseEntity<?> allPermission() {
        User user = authenticatedUser.auth();

        if(!user.getIsAdmin()){
            return response.failResponse("Permission denied", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().body(permissionRepository.findAll());
    }

    @Override
    public ResponseEntity<?> updatePermistion(String Id, PermissionUpdateModel permissionUpdateModel) {
        User user = authenticatedUser.auth();

        if(!user.getIsAdmin()){
            return response.failResponse("Permission denied", HttpStatus.BAD_REQUEST);
        }
         AdminUser adminUser = adminUserRepository.findByUserId(user.getId());
         List<String> createPermissionlist = Arrays.asList("update", "all");
        
        List<String> myPermission = adminUser.getPermission();
        for(String id: myPermission){
            if(!createPermissionlist.contains(helper.getRole(id))){
                return response.failResponse("You don't have permission to perform this opeation", HttpStatus.BAD_REQUEST);
            }
        }

        Permission permission = permissionRepository.findPermissionById(Id);
        if(Objects.isNull(permission)){
            return response.failResponse("Invalid id", HttpStatus.BAD_REQUEST);
        }
        permission.setName(permissionUpdateModel.getName());
        permissionRepository.save(permission);
        return response.successResponse("Permission updated successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> deletePermission(String Id) {
        User user = authenticatedUser.auth();

        if(!user.getIsAdmin()){
            return response.failResponse("Permission denied", HttpStatus.BAD_REQUEST);
        }

        AdminUser adminUser = adminUserRepository.findByUserId(user.getId());
        List<String> createPermissionlist = Arrays.asList("delete", "all");
        
        List<String> myPermission = adminUser.getPermission();
        for(String id: myPermission){
            if(!createPermissionlist.contains(helper.getRole(id))){
                return response.failResponse("You don't have permission to perform this opeation", HttpStatus.BAD_REQUEST);
            }
        }

        Permission permission = permissionRepository.findPermissionById(Id);
        if(Objects.isNull(permission)){
            return response.failResponse("Invalid id", HttpStatus.BAD_REQUEST);
        }

        if(Objects.equals(permission.getRole(), "all")){
            return response.failResponse("You can not remove the super admin", HttpStatus.OK);
        }

        permissionRepository.delete(permission);
        return response.successResponse("Permission deleted", HttpStatus.OK);
    }
}
