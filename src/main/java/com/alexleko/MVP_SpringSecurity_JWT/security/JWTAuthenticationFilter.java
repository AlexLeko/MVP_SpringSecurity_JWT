package com.alexleko.MVP_SpringSecurity_JWT.security;

import com.alexleko.MVP_SpringSecurity_JWT.dto.CredenciaisDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private JWTUtil jwtUtil;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            // recupera da requição os dados do usuario e converte para a class CredenciaisDTO.
            CredenciaisDTO creds = new ObjectMapper()
                                        .readValue(request.getInputStream(), CredenciaisDTO.class);

            // Cria um objeto para autenticação.
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                                                    creds.getEmail(),
                                                                    creds.getSenha(),
                                                                    new ArrayList<>());

            // valida se os dados são validos.
            Authentication auth = authenticationManager.authenticate(authToken);

            return auth;
        } catch (IOException e) {
            throw  new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        // recupera o user do spring security e passa o e-mail.
        String username = ((User) auth.getPrincipal()).getUsername();

        String token = jwtUtil.generationToken(username);

        // Cria no cabeçalho da requisição o Authorization com o token.
        response.addHeader("Authorization", "Bearer " + token);

        // Cria o Cabeçalho AUTHORIZATION nos padrões do CORS
        response.addHeader("access-control-expose-headers", "Authorization");
    }

    // Implementação para Spring-Boot 2.0 -
    private class JWTAuthenticationFailureHandler implements AuthenticationFailureHandler {

        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
                throws IOException, ServletException {
            response.setStatus(401);
            response.setContentType("application/json");
            response.getWriter().append(json());
        }

        private String json() {
            long date = new Date().getTime();
            return "{\"timestamp\": " + date + ", "
                    + "\"status\": 401, "
                    + "\"error\": \"Não autorizado\", "
                    + "\"message\": \"Email ou senha inválidos\", "
                    + "\"path\": \"/login\"}";
        }
    }


}
