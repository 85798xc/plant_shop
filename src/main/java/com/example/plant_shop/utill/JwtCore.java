package com.example.plant_shop.utill;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class JwtCore {

    @Value("${plant_shop.app.secret}")
    private String secret;

    @Value("${plant_shop.app.lifetime}")
    private int lifetime;

    public String generateToken(Authentication authentication){
        UserDetailsImpl userDetails= (UserDetailsImpl) authentication.getPrincipal();
        return Jwts.builder().setSubject();
    }

}
