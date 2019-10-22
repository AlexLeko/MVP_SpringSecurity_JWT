package com.alexleko.MVP_SpringSecurity_JWT.security;

import io.jsonwebtoken.Claims;
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


    // Valida se as claims do token estão válidos.
    public boolean tokenValido(String token) {
        Claims claims = getClaims(token);

        if (claims != null) {
            String username = claims.getSubject();
            Date expirationDate = claims.getExpiration();
            Date now = new Date(System.currentTimeMillis());

            // Valida se o token esta no periodo valido de expiração.
            if (username != null && expirationDate != null && now.before(expirationDate)) {
                return true;
            }
        }

        return false;
    }

    // [Claims] => Armazena as reivindicações(Claims) do token (usuario e tempo de expiração)
    private Claims getClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(secret.getBytes())
                    .parseClaimsJws(token).getBody();
        } catch (Exception exc) {
            return null;
        }
    }

    // Recupera o usuário pelas Claims de seu Token.
    public String getUsername(String token) {
        Claims claims = getClaims(token);
        if (claims != null) {
            return claims.getSubject();
        }

        return null;
    }




}
