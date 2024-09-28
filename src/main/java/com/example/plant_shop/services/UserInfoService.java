package com.example.plant_shop.services;

import com.example.plant_shop.entity.UserInfo;
import com.example.plant_shop.entity.UserInfoDetails;
import com.example.plant_shop.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserInfoService implements UserDetailsService {

    private final UserInfoRepository repository;
    private final PasswordEncoder encoder;

    // Constructor-based injection to avoid potential circular dependencies
    public UserInfoService(UserInfoRepository repository, @Lazy PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserInfo> userDetail = repository.findByUsername(username); 

        // Converting UserInfo to UserDetails
        return userDetail.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    public String addUser(UserInfo userInfo) {
        // Check if username or email already exists
        Optional<UserInfo> existingUserByUsername = repository.findByUsername(userInfo.getUsername());
        Optional<UserInfo> existingUserByEmail = repository.findByEmail(userInfo.getEmail());

        if (existingUserByUsername.isPresent()) {
            return "Username already exists";
        }

        if (existingUserByEmail.isPresent()) {
            return "Email already exists";
        }

        try {
            // Encode password before saving the user
            userInfo.setPassword(encoder.encode(userInfo.getPassword()));
            repository.save(userInfo);
            return "User Added Successfully";
        } catch (DataIntegrityViolationException e) {
            return "Error: Unable to add user due to a database constraint violation.";
        }
    }
}