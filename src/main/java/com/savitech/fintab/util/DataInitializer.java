package com.savitech.fintab.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import com.savitech.fintab.entity.AdminUser;
import com.savitech.fintab.entity.Permission;
import com.savitech.fintab.entity.User;
import com.savitech.fintab.repository.AdminUserRepository;
import com.savitech.fintab.repository.PermissionRepository;
import com.savitech.fintab.repository.UserRepository;


@Component
public class DataInitializer implements ApplicationRunner{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    public void run(ApplicationArguments args) throws Exception{
        String email = "admin@fintabsolution.com";
        if(!userRepository.existsByEmail(email)){
            User user = new User();
            user.setEmail("admin@fintabsolution.com");
            user.setPassword(passwordEncoder.encode("12345"));
            user.setIsAdmin(true);
            userRepository.save(user);
            
            // Create Super Admin permission
            Permission perm = null;
            if(!permissionRepository.existsByName("admin")){
                Permission userPerm = new Permission();
                userPerm.setName("admin");
                userPerm.setRole("all");
                permissionRepository.save(userPerm);

                perm = userPerm;
            }

            AdminUser adminUser = new AdminUser();

            List<String> userPermission = new ArrayList<>();
            userPermission.add(perm.getId());
            adminUser.setFirstName("System");
            adminUser.setLastName("Administrator");
            adminUser.setDepartment("Admin");
            adminUser.setUser(user);
            adminUser.setPermission(userPermission);
            adminUserRepository.save(adminUser);
        }
    }
}
