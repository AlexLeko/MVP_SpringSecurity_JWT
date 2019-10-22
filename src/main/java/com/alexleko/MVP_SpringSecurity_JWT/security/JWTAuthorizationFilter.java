package com.alexleko.MVP_SpringSecurity_JWT.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private JWTUtil jwtUtil;
    private UserDetailsService userDetailsService;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager,
                                  JWTUtil jwtUtil, UserDetailsService userDetailsService) {

        super(authenticationManager);
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        // recupera o valor do token no cabeçalho "Authorization" que vem na requisição.
        String header = request.getHeader("Authorization");

        // Verifica se é um header válido.
        if (header != null && header.startsWith("Bearer ")) {
            // recupera somente o token do header.
            UsernamePasswordAuthenticationToken auth = getAuthentication(header.substring(7));

            if (auth != null) {
                // libera o acesso ao filter.
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String token) {

        if (jwtUtil.tokenValido(token)) {
            // recupera o usuario pelo token
            String username = jwtUtil.getUsername(token);
            UserDetails user = userDetailsService.loadUserByUsername(username);

            return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        }

        return null;
    }





}
