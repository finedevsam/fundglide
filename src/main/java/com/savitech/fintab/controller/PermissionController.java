package com.savitech.fintab.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.savitech.fintab.dto.PermissionDto;
import com.savitech.fintab.dto.PermissionUpdateDto;
import com.savitech.fintab.impl.PermissionManagerServiceImpl;


@RestController
@RequestMapping("auth/permission")
public class PermissionController {
    
    @Autowired
    private PermissionManagerServiceImpl permissionManagerServiceImpl;

    @PostMapping()
    public ResponseEntity<?> createNewRole(@RequestBody PermissionDto permissionModel){
        return permissionManagerServiceImpl.createRole(permissionModel);
    }

    @GetMapping
    public ResponseEntity<?> allPermission(){
        return permissionManagerServiceImpl.allPermission();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePermission(@PathVariable String id, @RequestBody PermissionUpdateDto permissionUpdateModel){
        return permissionManagerServiceImpl.updatePermistion(id, permissionUpdateModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePermission(@PathVariable String id){
        return permissionManagerServiceImpl.deletePermission(id);
    }
}
