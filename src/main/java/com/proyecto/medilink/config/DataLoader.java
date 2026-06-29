package com.proyecto.medilink.config;

import com.proyecto.medilink.model.Rol;
import com.proyecto.medilink.model.Usuario;
import com.proyecto.medilink.repository.RolRepository;
import com.proyecto.medilink.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.mindrot.jbcrypt.BCrypt;

@Component
public class DataLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String ROLE_USER = "ROLE_USER";
    private static final String ADMIN_EMAIL = "admin@admin.com";

    private final RolRepository rolRepo;
    private final UsuarioRepository usuarioRepo;

    // ✅ Constructor injection (en lugar de @Autowired en campos)
    public DataLoader(RolRepository rolRepo, UsuarioRepository usuarioRepo) {
        this.rolRepo = rolRepo;
        this.usuarioRepo = usuarioRepo;
    }

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
            
            // 🔒 La contraseña se lee desde variable de entorno
            String adminPassword = System.getenv("ADMIN_PASSWORD");
            
            // ⚠️ Si no existe variable de entorno, USAR SOLO PARA DESARROLLO
            if (adminPassword == null || adminPassword.isEmpty()) {
                adminPassword = "admin123"; // Solo para desarrollo local
                logger.warn("⚠️  ADVERTENCIA: Usando contraseña por defecto. Setea ADMIN_PASSWORD en producción.");
            }
            
            admin.setPassword(BCrypt.hashpw(adminPassword, BCrypt.gensalt()));
            admin.setRol(rolRepo.findByNombre(ROLE_ADMIN));
            usuarioRepo.save(admin);
            logger.info("✅ Administrador creado con éxito.");
        } else {
            // Actualizar rol si es necesario
            Usuario existing = usuarioRepo.findByEmail(ADMIN_EMAIL);
            if (existing != null && (existing.getRol() == null || !ROLE_ADMIN.equals(existing.getRol().getNombre()))) {
                existing.setRol(rolRepo.findByNombre(ROLE_ADMIN));
                usuarioRepo.save(existing);
                logger.info("✅ Rol de administrador actualizado.");
            }
        }
    }
}