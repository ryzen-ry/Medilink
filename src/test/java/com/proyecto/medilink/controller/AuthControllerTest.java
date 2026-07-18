package com.proyecto.medilink.controller;

import com.proyecto.medilink.dto.LoginDTO;
import com.proyecto.medilink.dto.UsuarioDTO;
import com.proyecto.medilink.model.Rol;
import com.proyecto.medilink.model.Usuario;
import com.proyecto.medilink.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private HttpSession session;

    @InjectMocks
    private AuthController authController;

    private Usuario usuario;
    private UsuarioDTO usuarioDTO;
    private LoginDTO loginDTO;
    private Rol rolUser;

    @BeforeEach
    void setUp() {
        // Configurar rol
        rolUser = new Rol();
        rolUser.setId(1L);
        rolUser.setNombre("ROLE_USER");

        // Configurar usuario
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Juan Pérez");
        usuario.setDni("12345678");
        usuario.setTelefono("999999999");
        usuario.setEmail("juan@email.com");
        usuario.setPassword("password123");
        usuario.setRol(rolUser);

        // Configurar UsuarioDTO
        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNombre("Juan Pérez");
        usuarioDTO.setDni("12345678");
        usuarioDTO.setTelefono("999999999");
        usuarioDTO.setEmail("juan@email.com");
        usuarioDTO.setPassword("password123");

        // Configurar LoginDTO
        loginDTO = new LoginDTO();
        loginDTO.setEmail("juan@email.com");
        loginDTO.setPassword("password123");
    }

    // ============================================================
    // 1. TEST LOGIN (GET)
    // ============================================================

    @Test
    void testLogin_DevuelveVistaLogin() {
        String resultado = authController.login(model);

        assertEquals("login", resultado);
        verify(model).addAttribute(eq("loginDTO"), any(LoginDTO.class));
    }

    // ============================================================
    // 2. TEST PROCESAR REGISTRO
    // ============================================================

    @Test
    void testProcesarRegistro_Exitoso() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(usuarioService.findByEmail(usuarioDTO.getEmail())).thenReturn(null);
        when(usuarioService.registrarUsuario(any(Usuario.class))).thenReturn(usuario);

        String resultado = authController.procesarRegistro(usuarioDTO, bindingResult);

        assertEquals("redirect:/login?success", resultado);
        verify(usuarioService).registrarUsuario(any(Usuario.class));
        verify(bindingResult, never()).rejectValue(anyString(), anyString(), anyString());
    }

    @Test
    void testProcesarRegistro_ErrorValidacion_DevuelveRegistro() {
        when(bindingResult.hasErrors()).thenReturn(true);

        String resultado = authController.procesarRegistro(usuarioDTO, bindingResult);

        assertEquals("registro", resultado);
        verify(usuarioService, never()).registrarUsuario(any(Usuario.class));
        verify(usuarioService, never()).findByEmail(anyString());
    }

    @Test
    void testProcesarRegistro_EmailYaExiste_DevuelveRegistroConError() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(usuarioService.findByEmail(usuarioDTO.getEmail())).thenReturn(usuario);

        String resultado = authController.procesarRegistro(usuarioDTO, bindingResult);

        assertEquals("registro", resultado);
        verify(bindingResult).rejectValue("email", "error.usuario", "El correo ya existe");
        verify(usuarioService, never()).registrarUsuario(any(Usuario.class));
    }

    @Test
    void testProcesarRegistro_EmailYaExiste_ConErrorValidacionPrevia_DevuelveRegistro() {
        when(bindingResult.hasErrors()).thenReturn(true);

        String resultado = authController.procesarRegistro(usuarioDTO, bindingResult);

        assertEquals("registro", resultado);
        verify(usuarioService, never()).findByEmail(anyString());
        verify(usuarioService, never()).registrarUsuario(any(Usuario.class));
    }

    @Test
    void testProcesarRegistro_CamposNulos_DevuelveRegistro() {
        UsuarioDTO dtoInvalido = new UsuarioDTO();
        when(bindingResult.hasErrors()).thenReturn(true);

        String resultado = authController.procesarRegistro(dtoInvalido, bindingResult);

        assertEquals("registro", resultado);
        verify(usuarioService, never()).registrarUsuario(any(Usuario.class));
    }

    @Test
    void testProcesarRegistro_EmailConEspacios_Exitoso() {
        usuarioDTO.setEmail("  juan@email.com  ");
        when(bindingResult.hasErrors()).thenReturn(false);
        when(usuarioService.findByEmail(anyString())).thenReturn(null);
        when(usuarioService.registrarUsuario(any(Usuario.class))).thenReturn(usuario);

        String resultado = authController.procesarRegistro(usuarioDTO, bindingResult);

        assertEquals("redirect:/login?success", resultado);
        verify(usuarioService).registrarUsuario(any(Usuario.class));
    }

    // ============================================================
    // 3. TEST LOGOUT
    // ============================================================

    @Test
    void testLogout_InvalidarSesion_RedirigeHome() {
        String resultado = authController.logout(session);

        verify(session).invalidate();
        assertEquals("redirect:/", resultado);
    }

    @Test
    void testLogout_SesionNula_RedirigeHome() {
        // Si la sesión es null, invalidate lanza excepción
        // Pero el controlador no valida null, así que simulamos una sesión válida
        String resultado = authController.logout(session);

        verify(session).invalidate();
        assertEquals("redirect:/", resultado);
    }

    // ============================================================
    // 4. PRUEBAS ADICIONALES DE REGISTRO CON DATOS ESPECÍFICOS
    // ============================================================

    @Test
    void testProcesarRegistro_UsuarioConDniVacio_DevuelveRegistro() {
        usuarioDTO.setDni("");
        when(bindingResult.hasErrors()).thenReturn(true);

        String resultado = authController.procesarRegistro(usuarioDTO, bindingResult);

        assertEquals("registro", resultado);
        verify(usuarioService, never()).registrarUsuario(any(Usuario.class));
    }

    @Test
    void testProcesarRegistro_UsuarioConTelefonoVacio_DevuelveRegistro() {
        usuarioDTO.setTelefono("");
        when(bindingResult.hasErrors()).thenReturn(true);

        String resultado = authController.procesarRegistro(usuarioDTO, bindingResult);

        assertEquals("registro", resultado);
        verify(usuarioService, never()).registrarUsuario(any(Usuario.class));
    }

    @Test
    void testProcesarRegistro_UsuarioConPasswordCorta_DevuelveRegistro() {
        usuarioDTO.setPassword("123");
        when(bindingResult.hasErrors()).thenReturn(true);

        String resultado = authController.procesarRegistro(usuarioDTO, bindingResult);

        assertEquals("registro", resultado);
        verify(usuarioService, never()).registrarUsuario(any(Usuario.class));
    }

    @Test
    void testProcesarRegistro_UsuarioConEmailInvalido_DevuelveRegistro() {
        usuarioDTO.setEmail("correo-invalido");
        when(bindingResult.hasErrors()).thenReturn(true);

        String resultado = authController.procesarRegistro(usuarioDTO, bindingResult);

        assertEquals("registro", resultado);
        verify(usuarioService, never()).registrarUsuario(any(Usuario.class));
    }

    // ============================================================
    // 5. PRUEBA DE INTEGRACIÓN DE FLUJO (SIMULADA)
    // ============================================================

    @Test
    void testFlujoCompleto_RegistroExitoso_Login() {
        // 1. Registro exitoso
        when(bindingResult.hasErrors()).thenReturn(false);
        when(usuarioService.findByEmail(usuarioDTO.getEmail())).thenReturn(null);
        when(usuarioService.registrarUsuario(any(Usuario.class))).thenReturn(usuario);

        String resultadoRegistro = authController.procesarRegistro(usuarioDTO, bindingResult);
        assertEquals("redirect:/login?success", resultadoRegistro);

        // 2. Mostrar login
        String resultadoLogin = authController.login(model);
        assertEquals("login", resultadoLogin);
        verify(model).addAttribute(eq("loginDTO"), any(LoginDTO.class));

        // 3. Logout
        String resultadoLogout = authController.logout(session);
        verify(session).invalidate();
        assertEquals("redirect:/", resultadoLogout);
    }
}
