package com.savitech.fintab.service;

import org.springframework.http.ResponseEntity;

import com.savitech.fintab.entity.impl.PermissionModel;
import com.savitech.fintab.entity.impl.PermissionUpdateModel;

public interface PermissionManagerService {
    ResponseEntity<?> createRole(PermissionModel permissionModel);

    ResponseEntity<?> allPermission();

    ResponseEntity<?> updatePermistion(String Id, PermissionUpdateModel permissionUpdateModel);
    
    ResponseEntity<?> deletePermission(String Id);

}
