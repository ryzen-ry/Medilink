package com.proyecto.medilink.service;

import com.proyecto.medilink.model.Rol;
import com.proyecto.medilink.model.Usuario;
import com.proyecto.medilink.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolService rolService;

    public UsuarioService(UsuarioRepository usuarioRepository, RolService rolService) {
        this.usuarioRepository = usuarioRepository;
        this.rolService = rolService;
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario registrarUsuario(Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        // Si se registra el email admin@admin.com, asignar rol ADMIN automáticamente
        if ("admin@admin.com".equalsIgnoreCase(usuario.getEmail())) {
            usuario.setRol(rolService.findByNombre("ROLE_ADMIN"));
        } else {
            usuario.setRol(rolService.findByNombre("ROLE_USER"));
        }

        return usuarioRepository.save(usuario);
    }

    public Usuario findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public List<Usuario> getAll() {
        return usuarioRepository.findAll();
    }

    public boolean validarCredenciales(String email, String pass) {
        Usuario u = usuarioRepository.findByEmail(email);
        return u != null && passwordEncoder.matches(pass, u.getPassword());
    }

    public void cambiarRol(Long usuarioId, String nuevoRol) {
        Usuario u = usuarioRepository.findById(usuarioId).orElse(null);
        if (u != null) {
            Rol rolNuevo = rolService.findByNombre(nuevoRol);
            u.setRol(rolNuevo);
            usuarioRepository.save(u);
        }
    }

    // ✅ NUEVO: Eliminar usuarios excepto al admin actual
    /**
     * Elimina un usuario por id si existe y no es el admin que realiza la acción.
     * Devuelve true si se eliminó, false si no se realizó ninguna acción.
     */
    public boolean eliminarUsuario(Long id, String adminEmail) {
        Usuario usuarioAEliminar = usuarioRepository.findById(id).orElse(null);
        if (usuarioAEliminar == null) return false;

        Usuario adminActual = usuarioRepository.findByEmail(adminEmail);
        if (adminActual == null) return false;

        if (usuarioAEliminar.getEmail().equals(adminActual.getEmail())) {
            // No permitir eliminar al admin que realiza la acción
            return false;
        }

        usuarioRepository.delete(usuarioAEliminar);
        return true;
    }
    
}
