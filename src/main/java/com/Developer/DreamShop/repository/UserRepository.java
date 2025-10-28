package com.Developer.DreamShop.repository;

import com.Developer.DreamShop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long > {
    boolean existsByEmail(String email);
}
