package com.alexleko.MVP_SpringSecurity_JWT.config;

import com.alexleko.MVP_SpringSecurity_JWT.security.JWTAuthenticationFilter;
import com.alexleko.MVP_SpringSecurity_JWT.security.JWTAuthorizationFilter;
import com.alexleko.MVP_SpringSecurity_JWT.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // permite utilizar o @PreAuthorize em cada endpoint.
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private Environment env;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JWTUtil jwtUtil;

    // ===================================================
    // endpoints com acesso livre, conforme o verbo REST.

    // caminhos de test
    private static final String[] PUBLIC_MATCHERS = {
            "/h2-console/**"
    };

    // caminhos de leitura [GET]
    private static final String[] PUBLIC_MATCHERS_GET = {
            "/clientes/**"          // *** liberado somente por questão de exemplo de teste ***
    };

    // cadastros permitidos [POST]
    private static final String[] PUBLIC_MATCHERS_POST = {
            "/clientes",
            "/auth/forgot/**"
    };
    // ===================================================


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // disabilita a autenticação no ambiente de test.
        // liberando o acesso do banco H2
        if (Arrays.asList(env.getActiveProfiles()).contains("test")) {
            http.headers().frameOptions().disable();
        }

        // libera as request de cross-origin "multiplas fontes" com configurações basicos.
        // ex.: Ambiente de test / App / front-end
        // desabilitar proteção a [CSRF] em sistemas stateless.
        http.cors().and().csrf().disable();

        // endpoints liberados sem autenticação.
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll()
                .antMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET).permitAll()
                .antMatchers(PUBLIC_MATCHERS).permitAll()
                .anyRequest().authenticated();


        // registrar o filtro de  === AUTENTICAÇÃO ===
        http.addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtUtil));


        // registrar o filtro de === AUTORIZAÇÃO ===
        http.addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtUtil, userDetailsService));


        // garante que o back-end não crie sessão de usuario.
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    // configure de AUTENTICAÇÃO JWT para a minha classe de service.
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();

        // Lista de metodos que são permitidos pelo CORS
        configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "OPTIONS"));

        // Acesso basico de cross-origin a todos os caminhos
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


}