package com.savitech.fintab.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.savitech.fintab.entity.impl.CreateDepartmentModel;
import com.savitech.fintab.service.impl.DepartmentSeviceImpl;

@RestController
@RequestMapping("admin/department")
public class DepartmentController {
    
    @Autowired
    private DepartmentSeviceImpl departmentSeviceImpl;

    @PostMapping()
    public ResponseEntity<?> createDepartment(@RequestBody CreateDepartmentModel departmentModel){
        return departmentSeviceImpl.createDepartment(departmentModel);
    }

    @GetMapping()
    public List<?> allDepartment(Pageable pageable){
        return departmentSeviceImpl.allDepartment(pageable).toList();
    }

    @PutMapping("/{Id}")
    public ResponseEntity<?> updateDepartment(@PathVariable String Id, @RequestBody CreateDepartmentModel departmentModel){
        return departmentSeviceImpl.updateDepartment(Id, departmentModel);
    }

    @DeleteMapping("/{Id}")
    public ResponseEntity<?> deleteDepartment(@PathVariable String Id){
        return departmentSeviceImpl.deleteDepartment(Id);
    }
}
