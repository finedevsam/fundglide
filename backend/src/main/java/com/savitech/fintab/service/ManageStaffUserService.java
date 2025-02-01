package com.savitech.fintab.service;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.savitech.fintab.dto.AddPermissionToStaffDto;
import com.savitech.fintab.dto.CreateStaffDto;
import com.savitech.fintab.dto.UpdateStaffDto;

public interface ManageStaffUserService {
    ResponseEntity<?> createStaff(CreateStaffDto staffModel);

    ResponseEntity<?> allStaff(Pageable pageable);

    ResponseEntity<?> adminUpdateStaff(String Id, UpdateStaffDto updateStaffModel);

    ResponseEntity<?> giveUserPermission(String Id, AddPermissionToStaffDto permissionToStaff);

    ResponseEntity<?> revokeStaffPermission(String staffId, String permissionId);
}
