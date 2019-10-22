package com.alexleko.MVP_SpringSecurity_JWT.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;


    public String generationToken(String username) {
        return Jwts.builder()                                                           // gera o token
                .setSubject(username)                                                   // define o usuario
                .setExpiration(new Date(System.currentTimeMillis() + expiration))     // momento de expiração do token
                .signWith(SignatureAlgorithm.HS512, secret.getBytes())  // assinatura do token (tipo algoritimo e segredo)
                .compact();
    }



}
