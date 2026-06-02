package com.proyecto.medilink.config;

import com.proyecto.medilink.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    // El encoder BCrypt (compatible con las contraseñas almacenadas con BCrypt)
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // AuthenticationProvider usando tu UserDetailsService y BCrypt
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider prov = new DaoAuthenticationProvider();
        prov.setUserDetailsService(userDetailsService);
        prov.setPasswordEncoder(passwordEncoder());
        return prov;
    }

    // AuthenticationManager (necesario en algunas versiones)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // Chain de seguridad
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authenticationProvider(authenticationProvider())

            .authorizeHttpRequests(auth -> auth
                // Páginas públicas
                .requestMatchers("/", "/principal", "/nosotros", "/registro", "/css/**", "/img/**", "/js/**").permitAll()
                .requestMatchers("/login", "/procesar-login").permitAll()
                
                // API REST - Endpoints públicos
                .requestMatchers("/api/v1/doctores").permitAll()
                .requestMatchers("/api/v1/doctores/**").permitAll()
                .requestMatchers("/api/v1/citas").permitAll()
                .requestMatchers("/api/v1/citas/**").permitAll()
                .requestMatchers("/api/v1/usuarios").permitAll()
                .requestMatchers("/api/v1/usuarios/**").permitAll()
                .requestMatchers("/api/v1/auth/login").permitAll()
                .requestMatchers("/api/v1/auth/logout").permitAll()
                
                // Rutas protegidas
                .requestMatchers("/ADMIN/**").hasRole("ADMIN")
                .requestMatchers("/USER/**").hasRole("USER")
                .anyRequest().authenticated()
            )

            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/procesar-login")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/redireccion", true)
                .failureUrl("/login?error")
                .permitAll()
            )

            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
            );

        return http.build();
    }
}
