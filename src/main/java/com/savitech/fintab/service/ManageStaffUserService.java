package com.savitech.fintab.service;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.savitech.fintab.entity.impl.AddPermissionToStaff;
import com.savitech.fintab.entity.impl.CreateStaffModel;
import com.savitech.fintab.entity.impl.UpdateStaffModel;

public interface ManageStaffUserService {
    ResponseEntity<?> createStaff(CreateStaffModel staffModel);

    ResponseEntity<?> allStaff(Pageable pageable);

    ResponseEntity<?> adminUpdateStaff(String Id, UpdateStaffModel updateStaffModel);

    ResponseEntity<?> giveUserPermission(String Id, AddPermissionToStaff permissionToStaff);

    ResponseEntity<?> revokeStaffPermission(String staffId, String permissionId);
}
