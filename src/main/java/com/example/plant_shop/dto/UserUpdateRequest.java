package com.example.plant_shop.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class UserUpdateRequest {
    private final String username;
    private final String email;
    private final String password;
}
