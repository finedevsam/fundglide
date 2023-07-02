package com.savitech.fintab.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.savitech.fintab.entity.AdminUser;
import com.savitech.fintab.entity.Department;
import com.savitech.fintab.entity.User;
import com.savitech.fintab.entity.impl.CreateDepartmentModel;
import com.savitech.fintab.repository.AdminUserRepository;
import com.savitech.fintab.repository.DepartmentRepository;
import com.savitech.fintab.service.DepartmentService;
import com.savitech.fintab.util.AuthenticatedUser;
import com.savitech.fintab.util.Helper;
import com.savitech.fintab.util.Response;

@Service
public class DepartmentSeviceImpl implements DepartmentService{

    @Autowired
    private AuthenticatedUser authenticatedUser;

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private Response response;


    @Autowired
    private Helper helper;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public ResponseEntity<?> createDepartment(CreateDepartmentModel departmentModel) {

        User user = authenticatedUser.auth();
        AdminUser adminUser = adminUserRepository.findByUserId(user.getId());

        if(!user.getIsAdmin()){
            return response.failResponse("You don't have permission to perform this opeation", HttpStatus.BAD_REQUEST);
        }

        if(adminUser.getPermission().size() < 1){
            return response.failResponse("You don't have permission to perform this opeation", HttpStatus.BAD_REQUEST);
        }

        List<String> createPermissionlist = Arrays.asList("create", "all");
        
        List<String> myPermission = adminUser.getPermission();
        for(String id: myPermission){
            if(!createPermissionlist.contains(helper.getRole(id))){
                return response.failResponse("You don't have permission to perform this opeation", HttpStatus.BAD_REQUEST);
            }
        }

        if(departmentRepository.existsByName(departmentModel.getName())){
            return response.failResponse("Department already exist", HttpStatus.BAD_REQUEST);
        }

        Department department = new Department();
        department.setName(departmentModel.getName());
        departmentRepository.save(department);
        return response.successResponse("Department created", HttpStatus.OK);
    }

    @Override
    public Page<Department> allDepartment(Pageable pageable) {
        return departmentRepository.findAll(pageable);
    }

    @Override
    public ResponseEntity<?> updateDepartment(String Id, CreateDepartmentModel departmentModel) {
        User user = authenticatedUser.auth();
        AdminUser adminUser = adminUserRepository.findByUserId(user.getId());

        if(!user.getIsAdmin()){
            return response.failResponse("You don't have permission to perform this opeation", HttpStatus.BAD_REQUEST);
        }

        if(adminUser.getPermission().size() < 1){
            return response.failResponse("You don't have permission to perform this opeation", HttpStatus.BAD_REQUEST);
        }

        List<String> createPermissionlist = Arrays.asList("update", "all");
        
        List<String> myPermission = adminUser.getPermission();
        for(String id: myPermission){
            if(!createPermissionlist.contains(helper.getRole(id))){
                return response.failResponse("You don't have permission to perform this opeation", HttpStatus.BAD_REQUEST);
            }
        }

        Department department = departmentRepository.findDepartmentById(Id);
        if(Objects.isNull(department)){
            return response.failResponse("Invalid Id", HttpStatus.BAD_GATEWAY);
        }

        if(Objects.equals(department.getName(), "admin")){
            return response.failResponse("You can't perform this operation on this default department", HttpStatus.BAD_REQUEST);
        }
        department.setName(departmentModel.getName());
        departmentRepository.save(department);
        return response.successResponse("Department updated successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> deleteDepartment(String Id) {
        User user = authenticatedUser.auth();

        if(!user.getIsAdmin()){
            return response.failResponse("You don't have permission to perform this opeation", HttpStatus.BAD_REQUEST);
        }
        
        AdminUser adminUser = adminUserRepository.findByUserId(user.getId());

        if(adminUser.getPermission().size() < 1){
            return response.failResponse("You don't have permission to perform this opeation", HttpStatus.BAD_REQUEST);
        }

        List<String> createPermissionlist = Arrays.asList("delete", "all");
        
        List<String> myPermission = adminUser.getPermission();
        for(String id: myPermission){
            if(!createPermissionlist.contains(helper.getRole(id))){
                return response.failResponse("You don't have permission to perform this opeation", HttpStatus.BAD_REQUEST);
            }
        }

        Department department = departmentRepository.findDepartmentById(Id);
        if(Objects.isNull(department)){
            return response.failResponse("Invalid Id", HttpStatus.BAD_GATEWAY);
        }

        if(Objects.equals(department.getName(), "admin")){
            return response.failResponse("You can't perform this operation on this default department", HttpStatus.BAD_REQUEST);
        }

        departmentRepository.delete(department);
        return response.successResponse("Department deleted successfully", HttpStatus.OK);
    }
    
}
