package com.savitech.fintab.service;

import org.springframework.http.ResponseEntity;

import com.savitech.fintab.dto.PermissionDto;
import com.savitech.fintab.dto.PermissionUpdateDto;

public interface PermissionManagerService {
    ResponseEntity<?> createRole(PermissionDto permissionModel);

    ResponseEntity<?> allPermission();

    ResponseEntity<?> updatePermistion(String Id, PermissionUpdateDto permissionUpdateModel);
    
    ResponseEntity<?> deletePermission(String Id);

}
