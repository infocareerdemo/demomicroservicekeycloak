package com.gatew.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@EnableMethodSecurity(jsr250Enabled = true)
public class SecurityConfig {

    @Autowired
    JwtAuthConfig jwtAuthConfig;

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) throws Exception {
        http.csrf(httpCsrf -> httpCsrf.disable())
            .cors(Customizer.withDefaults())
            .authorizeExchange(exchanges ->
                exchanges.pathMatchers("/auth/**", "/register/**", "/employee/**", "/address/**").permitAll()
                        .pathMatchers("/dashboard/userHome").hasRole("user")
                        .pathMatchers("/dashboard/adminHome").hasRole("admin")
                        .pathMatchers("/product/user-product").hasRole("user")
                        .pathMatchers("/home/user-home").hasRole("user")
                        .anyExchange().authenticated())
            .oauth2ResourceServer(oauth -> oauth.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConfig)));

        return http.build();
    }
}
