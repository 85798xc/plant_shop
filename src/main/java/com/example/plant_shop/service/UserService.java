package com.example.plant_shop.service;

import com.example.plant_shop.dto.UserUpdateRequest;
import com.example.plant_shop.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserDetails loadUserByUsername(String username);
    User findUserByUsername(String username);
    Page<User> searchUserByText(String text, Pageable pageable);
    User findUserById(long id);
    Page<User> getUsers(Pageable pageable);
    User saveUser(User user);
    User updateUser(String username, UserUpdateRequest request);
    void deleteUser(String username);
    void loadAvatar(String username, String uri);
}