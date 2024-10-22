package com.example.plant_shop.controller;

import com.example.plant_shop.model.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserInfoService service;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    // Constructor-based injection
    public UserController(UserInfoService service, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.service = service;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/main")
    public String welcome() {
        return "Welcome to the main page";
    }

    @PostMapping("/addNewUser")
    public String addNewUser(@RequestBody User user) {
        return service.addUser(user);
    }

    @GetMapping("/user/userProfile")
    public String userProfile() {
        return "Welcome to User Profile";
    }

    @GetMapping("/admin/adminProfile")
    public String adminProfile() {
        return "Welcome to Admin Profile";
    }

    @PostMapping("/generateToken")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
            if (authentication.isAuthenticated()) {
                return jwtService.generateToken(authRequest.getUsername());
            } else {
                return "Authentication failed!";
            }
        } catch (Exception e) {
            throw new RuntimeException("Invalid user credentials!", e);
        }
    }
}
