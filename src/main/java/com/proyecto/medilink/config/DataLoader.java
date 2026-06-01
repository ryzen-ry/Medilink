package com.proyecto.medilink.config;

import com.proyecto.medilink.model.Rol;
import com.proyecto.medilink.model.Usuario;
import com.proyecto.medilink.repository.RolRepository;
import com.proyecto.medilink.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    RolRepository rolRepo;

    @Autowired
    UsuarioRepository usuarioRepo;

    @Override
    public void run(String... args) throws Exception {

        if (rolRepo.findByNombre("ROLE_ADMIN") == null) {
            Rol r = new Rol();
            r.setNombre("ROLE_ADMIN");
            rolRepo.save(r);
        }

        if (rolRepo.findByNombre("ROLE_USER") == null) {
            Rol r = new Rol();
            r.setNombre("ROLE_USER");
            rolRepo.save(r);
        }

        if (usuarioRepo.findByEmail("admin@admin.com") == null) {
            Usuario admin = new Usuario();
            admin.setNombre("Administrador");
            admin.setDni("00000000");
            admin.setTelefono("999999999");
            admin.setEmail("admin@admin.com");
            admin.setPassword(BCrypt.hashpw("admin123", BCrypt.gensalt()));
            admin.setRol(rolRepo.findByNombre("ROLE_ADMIN"));
            usuarioRepo.save(admin);
        } else {
            // Si ya existe el usuario admin pero no tiene rol ADMIN, actualizarlo.
            Usuario existing = usuarioRepo.findByEmail("admin@admin.com");
            if (existing != null && (existing.getRol() == null || !"ROLE_ADMIN".equals(existing.getRol().getNombre()))) {
                existing.setRol(rolRepo.findByNombre("ROLE_ADMIN"));
                usuarioRepo.save(existing);
            }
        }
    }
}
