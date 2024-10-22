package com.example.plant_shop.repository;

import com.example.plant_shop.model.role.ERole;
import com.example.plant_shop.model.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleName(ERole roleName);
}
