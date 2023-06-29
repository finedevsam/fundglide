package com.savitech.fintab.service;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.savitech.fintab.entity.impl.CreateStaffModel;
import com.savitech.fintab.entity.impl.UpdateStaffModel;

public interface ManageStaffUserService {
    ResponseEntity<?> createStaff(CreateStaffModel staffModel);

    ResponseEntity<?> allStaff(Pageable pageable);

    ResponseEntity<?> adminUpdateStaff(String Id, UpdateStaffModel updateStaffModel);
}
