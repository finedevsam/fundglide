package com.savitech.fintab.service;

import org.springframework.http.ResponseEntity;

import com.savitech.fintab.entity.Permission;
import com.savitech.fintab.entity.impl.PermissionModel;

public interface PermissionManagerService {
    ResponseEntity<?> createRole(PermissionModel permissionModel);

    ResponseEntity<?> allPermission();
}
