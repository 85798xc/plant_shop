package com.example.plant_shop.controller;

import com.example.plant_shop.config.jwt.JwtProvider;
import com.example.plant_shop.dto.JwtResponse;
import com.example.plant_shop.dto.LoginRequest;
import com.example.plant_shop.dto.UserDataRequest;
import com.example.plant_shop.model.User;
import com.example.plant_shop.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final AuthService registrationService;

    //"Авторизация пользователя"
    @PostMapping("/login")
    public ResponseEntity<?> authUser(@RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()));

        String jwt = jwtProvider.generateJwtToken(authentication);
        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(
                jwt,
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRoles()));
    }

    //"Регистрация"
    @PostMapping("/register")
    public ResponseEntity<?> registration(@RequestBody UserDataRequest registrationRequest) {
        return ResponseEntity.ok(registrationService.register(registrationRequest));
    }

    //"Выход из аккаунта"
    @GetMapping("/logout")
    public ResponseEntity<?> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().body(HttpStatus.OK);
    }
}
