package com.savitech.fintab.repository;

import com.savitech.fintab.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    User findUserByEmail(String email);

    User findUserById(String id);
}
