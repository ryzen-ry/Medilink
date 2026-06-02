package com.proyecto.medilink.service;

import com.proyecto.medilink.model.Usuario;
import com.proyecto.medilink.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario u = usuarioRepository.findByEmail(email);
        if (u == null) throw new UsernameNotFoundException("Usuario no encontrado: " + email);

        // Tu rol viene como "ROLE_ADMIN" o "ROLE_USER": quitar prefijo para .roles(...)
        String roleName = u.getRol() != null ? u.getRol().getNombre().replace("ROLE_", "") : "USER";

        return User.builder()
                .username(u.getEmail())
                .password(u.getPassword()) // ya debe venir en BCrypt
                .roles(roleName)
                .build();
    }
}
