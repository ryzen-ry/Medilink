package com.proyecto.medilink.config;

import com.proyecto.medilink.model.Rol;
import com.proyecto.medilink.model.Usuario;
import com.proyecto.medilink.repository.RolRepository;
import com.proyecto.medilink.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
@Component
public class DataLoader implements CommandLineRunner {

    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String ROLE_USER = "ROLE_USER";
    private static final String ADMIN_EMAIL = "admin@admin.com";

    private final RolRepository rolRepo;
    private final UsuarioRepository usuarioRepo;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(RolRepository rolRepo, UsuarioRepository usuarioRepo, PasswordEncoder passwordEncoder) {
        this.rolRepo = rolRepo;
        this.usuarioRepo = usuarioRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        // Crear roles si no existen
        if (rolRepo.findByNombre(ROLE_ADMIN) == null) {
            Rol r = new Rol();
            r.setNombre(ROLE_ADMIN);
            rolRepo.save(r);
        }

        if (rolRepo.findByNombre(ROLE_USER) == null) {
            Rol r = new Rol();
            r.setNombre(ROLE_USER);
            rolRepo.save(r);
        }

        // Crear admin solo si no existe
        if (usuarioRepo.findByEmail(ADMIN_EMAIL) == null) {
            Usuario admin = new Usuario();
            admin.setNombre("Administrador");
            admin.setDni("00000000");
            admin.setTelefono("999999999");
            admin.setEmail(ADMIN_EMAIL);
            
            String adminPassword = System.getenv("ADMIN_PASSWORD");

            if (adminPassword == null || adminPassword.isBlank()) {
                throw new IllegalStateException("ADMIN_PASSWORD debe estar definido como variable de entorno para crear el usuario administrador.");
            }

            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setRol(rolRepo.findByNombre(ROLE_ADMIN));
            usuarioRepo.save(admin);
            System.out.println("✅ Administrador creado con éxito.");
        } else {
            // Actualizar rol si es necesario
            Usuario existing = usuarioRepo.findByEmail(ADMIN_EMAIL);
            if (existing != null && (existing.getRol() == null || !ROLE_ADMIN.equals(existing.getRol().getNombre()))) {
                existing.setRol(rolRepo.findByNombre(ROLE_ADMIN));
                usuarioRepo.save(existing);
                System.out.println("✅ Rol de administrador actualizado.");
            }
        }
    }
}