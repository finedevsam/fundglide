package com.savitech.fintab.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.savitech.fintab.entity.impl.PermissionModel;
import com.savitech.fintab.service.impl.PermissionManagerServiceImpl;


@RestController
@RequestMapping("auth/permission")
public class PermissionController {
    
    @Autowired
    private PermissionManagerServiceImpl permissionManagerServiceImpl;

    @PostMapping()
    public ResponseEntity<?> createNewRole(@RequestBody PermissionModel permissionModel){
        return permissionManagerServiceImpl.createRole(permissionModel);
    }

    @GetMapping
    public ResponseEntity<?> allPermission(){
        return permissionManagerServiceImpl.allPermission();
    }
}
