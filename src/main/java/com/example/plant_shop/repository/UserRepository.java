package com.example.plant_shop.repository;

import com.example.plant_shop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
        Optional<User> findByEmail(String email);
}
