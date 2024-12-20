package com.example.plant_shop.service;

import com.example.plant_shop.dto.UserUpdateRequest;
import com.example.plant_shop.model.User;
import com.example.plant_shop.repository.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findUserByUsername(username);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepo.findByUsername(username)
                .orElseThrow(() ->
                        new EntityNotFoundException("Пользователь с именем " + username + " не найден!"));
    }

    @Override
    public Page<User> searchUserByText(String text, Pageable pageable) {
        return userRepo.searchUserByText(text, pageable);
    }

    @Override
    public User findUserById(long id) {
        return userRepo.findUserById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id = " + id + "не найден!"));
    }

    @Override
    public Page<User> getUsers(Pageable pageable) {
        return userRepo.findAll(pageable);
    }

    @Override
    public User saveUser(User user) {
        try {
            return userRepo.save(user);
        } catch (RuntimeException e) {
            log.error("Пользователь {} не сохранён! Error: [{}].", user.getUsername(), e);
            throw new PersistenceException(String.format("Пользователь %s не сохранён! " +
                    "Error: [%s]", user.getUsername(), e));
        }
    }

    @Override
    public User updateUser(String username, UserUpdateRequest request) {
        User user = findUserByUsername(username);
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return saveUser(user);
    }

    @Override
    public void deleteUser(String username) {
        if (userRepo.existsByUsername(username)) {
            userRepo.deleteByUsername(username);
        } else throw new EntityNotFoundException("Пользователь с именем \"" + username + "\" не найден!");
    }

    @Override
    public void loadAvatar(String username, String uri) {
        User user = findUserByUsername(username);
        user.setAvatar(uri);
        saveUser(user);
    }
}