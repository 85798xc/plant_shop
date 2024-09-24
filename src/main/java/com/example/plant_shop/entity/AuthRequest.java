package com.example.plant_shop.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor //nado protestit somnenija po rabote noargscontrusctor
public class AuthRequest {
    private String email;
    private String password;
}
