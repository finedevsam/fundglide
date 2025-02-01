package com.savitech.fintab.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.savitech.fintab.dto.AddPermissionToStaffDto;
import com.savitech.fintab.dto.CreateStaffDto;
import com.savitech.fintab.dto.UpdateStaffDto;
import com.savitech.fintab.impl.ManageStaffUserServiceImpl;

@RestController
@RequestMapping("admin/staff")
public class StaffController {

    @Autowired
    private ManageStaffUserServiceImpl staffUserServiceImpl;

    @PostMapping()
    public ResponseEntity<?> createStaff(@RequestBody CreateStaffDto staffModel){
        return staffUserServiceImpl.createStaff(staffModel);
    }

    @GetMapping()
    public ResponseEntity<?> allStaff(Pageable pageable){
        return staffUserServiceImpl.allStaff(pageable);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> adminUpdateStaff(@PathVariable String id, @RequestBody UpdateStaffDto updateStaffModel){
        return staffUserServiceImpl.adminUpdateStaff(id, updateStaffModel);
    }

    @PutMapping("permission/{id}/asign")
    public ResponseEntity<?> addPermissionToStaff(@PathVariable String id, @RequestBody AddPermissionToStaffDto addPermissionToStaff){
        return staffUserServiceImpl.giveUserPermission(id, addPermissionToStaff);
    }

    @GetMapping("permission/{staffId}/revoke/{permissionId}")
    public ResponseEntity<?> revokePermissionFromStaff(@PathVariable String staffId, @PathVariable String permissionId){
        return staffUserServiceImpl.revokeStaffPermission(staffId, permissionId);
    }
    
}
