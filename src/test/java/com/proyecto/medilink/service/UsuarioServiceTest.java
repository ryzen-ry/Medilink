package com.proyecto.medilink.service;

import com.proyecto.medilink.model.Rol;
import com.proyecto.medilink.model.Usuario;
import com.proyecto.medilink.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RolService rolService;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;
    private Usuario adminUsuario;
    private Rol rolUser;
    private Rol rolAdmin;

    @BeforeEach
    void setUp() {
        // Configurar roles
        rolUser = new Rol();
        rolUser.setId(1L);
        rolUser.setNombre("ROLE_USER");

        rolAdmin = new Rol();
        rolAdmin.setId(2L);
        rolAdmin.setNombre("ROLE_ADMIN");

        // Configurar usuario normal
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Juan Pérez");
        usuario.setEmail("juan@email.com");
        usuario.setPassword("password123");
        usuario.setRol(rolUser);

        // Configurar admin
        adminUsuario = new Usuario();
        adminUsuario.setId(2L);
        adminUsuario.setNombre("Administrador");
        adminUsuario.setEmail("admin@admin.com");
        adminUsuario.setPassword("admin123");
        adminUsuario.setRol(rolAdmin);
    }

    // ============================================================
    // 1. TEST REGISTRAR USUARIO
    // ============================================================

    @Test
    void testRegistrarUsuario_UsuarioNormal_AsignaRolUser() {
        String passwordOriginal = usuario.getPassword();
        when(rolService.findByNombre("ROLE_USER")).thenReturn(rolUser);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario resultado = usuarioService.registrarUsuario(usuario);

        assertNotNull(resultado);
        assertEquals(rolUser, resultado.getRol());
        assertNotEquals(passwordOriginal, resultado.getPassword());
        assertTrue(BCrypt.checkpw("password123", resultado.getPassword()));
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void testRegistrarUsuario_AdminEmail_AsignaRolAdmin() {
        Usuario admin = new Usuario();
        admin.setEmail("admin@admin.com");
        admin.setPassword("admin123");

        when(rolService.findByNombre("ROLE_ADMIN")).thenReturn(rolAdmin);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(admin);

        Usuario resultado = usuarioService.registrarUsuario(admin);

        assertNotNull(resultado);
        assertEquals(rolAdmin, resultado.getRol());
        verify(rolService).findByNombre("ROLE_ADMIN");
    }

    @Test
    void testRegistrarUsuario_EmailAdminMayusculas_AsignaRolAdmin() {
        Usuario admin = new Usuario();
        admin.setEmail("ADMIN@ADMIN.COM");
        admin.setPassword("admin123");

        when(rolService.findByNombre("ROLE_ADMIN")).thenReturn(rolAdmin);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(admin);

        Usuario resultado = usuarioService.registrarUsuario(admin);

        assertEquals(rolAdmin, resultado.getRol());
        verify(rolService).findByNombre("ROLE_ADMIN");
    }

    // ============================================================
    // 2. TEST FIND BY EMAIL
    // ============================================================

    @Test
    void testFindByEmail_UsuarioExiste_DevuelveUsuario() {
        String email = "juan@email.com";
        when(usuarioRepository.findByEmail(email)).thenReturn(usuario);

        Usuario resultado = usuarioService.findByEmail(email);

        assertNotNull(resultado);
        assertEquals(email, resultado.getEmail());
        verify(usuarioRepository).findByEmail(email);
    }

    @Test
    void testFindByEmail_UsuarioNoExiste_DevuelveNull() {
        String email = "noexiste@email.com";
        when(usuarioRepository.findByEmail(email)).thenReturn(null);

        Usuario resultado = usuarioService.findByEmail(email);

        assertNull(resultado);
        verify(usuarioRepository).findByEmail(email);
    }

    // ============================================================
    // 3. TEST GET ALL
    // ============================================================

    @Test
    void testGetAll_DevuelveListaUsuarios() {
        List<Usuario> usuarios = Arrays.asList(usuario, adminUsuario);
        when(usuarioRepository.findAll()).thenReturn(usuarios);

        List<Usuario> resultado = usuarioService.getAll();

        assertEquals(2, resultado.size());
        verify(usuarioRepository).findAll();
    }

    @Test
    void testGetAll_ListaVacia() {
        when(usuarioRepository.findAll()).thenReturn(Arrays.asList());

        List<Usuario> resultado = usuarioService.getAll();

        assertTrue(resultado.isEmpty());
        verify(usuarioRepository).findAll();
    }

    // ============================================================
    // 4. TEST VALIDAR CREDENCIALES
    // ============================================================

    @Test
    void testValidarCredenciales_CredencialesCorrectas_DevuelveTrue() {
        String passwordOriginal = usuario.getPassword();
        usuario.setPassword(BCrypt.hashpw(passwordOriginal, BCrypt.gensalt()));

        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(usuario);

        boolean resultado = usuarioService.validarCredenciales(usuario.getEmail(), passwordOriginal);

        assertTrue(resultado);
        verify(usuarioRepository).findByEmail(usuario.getEmail());
    }

    @Test
    void testValidarCredenciales_PasswordIncorrecta_DevuelveFalse() {
        // ✅ Generar hash válido para la prueba
        String passwordCorrecta = "password123";
        String hashValido = BCrypt.hashpw(passwordCorrecta, BCrypt.gensalt());
        usuario.setPassword(hashValido);

        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(usuario);

        boolean resultado = usuarioService.validarCredenciales(usuario.getEmail(), "passwordIncorrecta");

        assertFalse(resultado);
        verify(usuarioRepository).findByEmail(usuario.getEmail());
    }

    @Test
    void testValidarCredenciales_UsuarioNoExiste_DevuelveFalse() {
        String email = "noexiste@email.com";
        when(usuarioRepository.findByEmail(email)).thenReturn(null);

        boolean resultado = usuarioService.validarCredenciales(email, "cualquier");

        assertFalse(resultado);
        verify(usuarioRepository).findByEmail(email);
    }

    // ============================================================
    // 5. TEST CAMBIAR ROL
    // ============================================================

    @Test
    void testCambiarRol_UsuarioExiste_CambiaRol() {
        Long usuarioId = 1L;
        String nuevoRol = "ROLE_ADMIN";

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(rolService.findByNombre(nuevoRol)).thenReturn(rolAdmin);

        usuarioService.cambiarRol(usuarioId, nuevoRol);

        assertEquals(rolAdmin, usuario.getRol());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void testCambiarRol_UsuarioNoExiste_NoHaceNada() {
        Long usuarioId = 999L;
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.empty());

        usuarioService.cambiarRol(usuarioId, "ROLE_ADMIN");

        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    // ============================================================
    // 6. TEST ELIMINAR USUARIO
    // ============================================================

    @Test
    void testEliminarUsuario_UsuarioExistenteYNoEsAdmin_DevuelveTrue() {
        Long usuarioId = 1L;
        String adminEmail = "admin@admin.com";

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.findByEmail(adminEmail)).thenReturn(adminUsuario);

        boolean resultado = usuarioService.eliminarUsuario(usuarioId, adminEmail);

        assertTrue(resultado);
        verify(usuarioRepository).delete(usuario);
    }

    @Test
    void testEliminarUsuario_UsuarioNoExiste_DevuelveFalse() {
        Long usuarioId = 999L;
        String adminEmail = "admin@admin.com";

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.empty());

        boolean resultado = usuarioService.eliminarUsuario(usuarioId, adminEmail);

        assertFalse(resultado);
        verify(usuarioRepository, never()).delete(any(Usuario.class));
    }

    @Test
    void testEliminarUsuario_AdminNoExiste_DevuelveFalse() {
        Long usuarioId = 1L;
        String adminEmail = "admin@admin.com";

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.findByEmail(adminEmail)).thenReturn(null);

        boolean resultado = usuarioService.eliminarUsuario(usuarioId, adminEmail);

        assertFalse(resultado);
        verify(usuarioRepository, never()).delete(any(Usuario.class));
    }

    @Test
    void testEliminarUsuario_IntentarEliminarAlAdmin_DevuelveFalse() {
        Long adminId = adminUsuario.getId();
        String adminEmail = adminUsuario.getEmail();

        when(usuarioRepository.findById(adminId)).thenReturn(Optional.of(adminUsuario));
        when(usuarioRepository.findByEmail(adminEmail)).thenReturn(adminUsuario);

        boolean resultado = usuarioService.eliminarUsuario(adminId, adminEmail);

        assertFalse(resultado);
        verify(usuarioRepository, never()).delete(any(Usuario.class));
    }

    @Test
    void testEliminarUsuario_AdminDistinto_DevuelveTrue() {
        Long usuarioId = 1L;
        String adminEmail = "otroadmin@admin.com";
        Usuario otroAdmin = new Usuario();
        otroAdmin.setEmail(adminEmail);

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.findByEmail(adminEmail)).thenReturn(otroAdmin);

        boolean resultado = usuarioService.eliminarUsuario(usuarioId, adminEmail);

        assertTrue(resultado);
        verify(usuarioRepository).delete(usuario);
    }
}
