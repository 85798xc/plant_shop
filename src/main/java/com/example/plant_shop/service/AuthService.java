package com.example.plant_shop.service;

import com.example.plant_shop.model.User;
import com.example.plant_shop.model.role.Role;
import com.example.plant_shop.repository.RoleRepo;
import com.example.plant_shop.repository.UserRepo;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;

    public User register(UserDataRequest registrationRequest) {
        User user = new User(registrationRequest);

        if (userRepo.existsByUsername(registrationRequest.getUsername())) {
            throw new EntityExistsException("Пользователь " + registrationRequest.getUsername() + " уже существует!");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Set<Role> roles = registrationRequest.getRoles().stream()
                .map(r -> roleRepo.findByRoleName(r)
                        .orElseThrow(() ->
                                new EntityNotFoundException("Роль " + r.name() + " не найдена!")))
                .collect(Collectors.toSet());

        user.setRoles(roles);
        userRepo.save(user);
        return user;
    }
}
