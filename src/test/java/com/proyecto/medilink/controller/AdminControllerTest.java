package com.proyecto.medilink.controller;

import com.proyecto.medilink.dto.DoctorDTO;
import com.proyecto.medilink.model.*;
import com.proyecto.medilink.service.*;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private DoctorService doctorService;

    @Mock
    private CitaService citaService;

    @Mock
    private ExamenService examenService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private MultipartFile imageFile;

    @InjectMocks
    private AdminController adminController;

    private Usuario adminUsuario;
    private Usuario normalUsuario;
    private Rol adminRol;
    private Doctor doctor;
    private Cita cita;

    @BeforeEach
    void setUp() {
        // Configurar rol admin
        adminRol = new Rol();
        adminRol.setNombre("ROLE_ADMIN");

        // Configurar usuario admin
        adminUsuario = new Usuario();
        adminUsuario.setId(1L);
        adminUsuario.setEmail("admin@admin.com");
        adminUsuario.setRol(adminRol);

        // Configurar usuario normal
        normalUsuario = new Usuario();
        normalUsuario.setId(2L);
        normalUsuario.setEmail("user@user.com");

        // Configurar doctor
        doctor = new Doctor();
        doctor.setId(1L);
        doctor.setNombre("Dr. Juan");
        doctor.setApellidos("Pérez");
        doctor.setEspecialidad("Cardiología");

        // Configurar cita
        cita = new Cita();
        cita.setId(1L);
        cita.setEstado("PENDIENTE");
    }

    // ============================================================
    // 1. TEST DASHBOARD
    // ============================================================

    @Test
    void testDashboard_UsuarioAdmin_DevuelveDashboard() {
        // GIVEN: Usuario admin logueado
        when(session.getAttribute("usuarioLogueado")).thenReturn(adminUsuario);
        when(usuarioService.getAll()).thenReturn(new ArrayList<>());
        when(doctorService.getAllDoctores()).thenReturn(new ArrayList<>());
        when(citaService.listarTodas()).thenReturn(new ArrayList<>());

        // WHEN
        String resultado = adminController.dashboard(session, model);

        // THEN
        assertEquals("ADMIN/dashboard", resultado);
        verify(model).addAttribute("usuarioLogueado", adminUsuario);
        verify(model).addAttribute("usuarios", new ArrayList<>());
        verify(model).addAttribute("doctores", new ArrayList<>());
        verify(model).addAttribute("citas", new ArrayList<>());
    }

    @Test
    void testDashboard_UsuarioNoAdmin_RedirigeLogin() {
        // GIVEN: Usuario sin rol admin
        when(session.getAttribute("usuarioLogueado")).thenReturn(normalUsuario);

        // WHEN
        String resultado = adminController.dashboard(session, model);

        // THEN
        assertEquals("redirect:/login", resultado);
        verify(model, never()).addAttribute(anyString(), any());
    }

    @Test
    void testDashboard_SinSesion_RedirigeLogin() {
        // GIVEN: No hay usuario en sesión
        when(session.getAttribute("usuarioLogueado")).thenReturn(null);

        // WHEN
        String resultado = adminController.dashboard(session, model);

        // THEN
        assertEquals("redirect:/login", resultado);
    }

    // ============================================================
    // 2. TEST LISTAR USUARIOS
    // ============================================================

    @Test
    void testListarUsuarios_UsuarioAdmin_RedirigeDashboard() {
        // GIVEN
        when(session.getAttribute("usuarioLogueado")).thenReturn(adminUsuario);

        // WHEN
        String resultado = adminController.listarUsuarios(session, model);

        // THEN
        assertEquals("redirect:/ADMIN/dashboard", resultado);
    }

    @Test
    void testListarUsuarios_UsuarioNoAdmin_RedirigeLogin() {
        // GIVEN
        when(session.getAttribute("usuarioLogueado")).thenReturn(normalUsuario);

        // WHEN
        String resultado = adminController.listarUsuarios(session, model);

        // THEN
        assertEquals("redirect:/login", resultado);
    }

    // ============================================================
    // 3. TEST CAMBIAR ROL
    // ============================================================

    @Test
    void testCambiarRol_UsuarioAdmin_CambiaRol() {
        // GIVEN
        Long userId = 2L;
        String nuevoRol = "ROLE_USER";
        when(session.getAttribute("usuarioLogueado")).thenReturn(adminUsuario);

        // WHEN
        String resultado = adminController.cambiarRol(userId, nuevoRol, session);

        // THEN
        verify(usuarioService).cambiarRol(userId, nuevoRol);
        assertEquals("redirect:/ADMIN/usuarios", resultado);
    }

    @Test
    void testCambiarRol_UsuarioNoAdmin_RedirigeLogin() {
        // GIVEN
        when(session.getAttribute("usuarioLogueado")).thenReturn(normalUsuario);

        // WHEN
        String resultado = adminController.cambiarRol(2L, "ROLE_USER", session);

        // THEN
        assertEquals("redirect:/login", resultado);
        verify(usuarioService, never()).cambiarRol(anyLong(), anyString());
    }

    // ============================================================
    // 4. TEST ELIMINAR USUARIO
    // ============================================================

    @Test
    void testEliminarUsuario_UsuarioAdmin_EliminaExitoso() {
        // GIVEN
        Long userId = 2L;
        when(session.getAttribute("usuarioLogueado")).thenReturn(adminUsuario);
        when(usuarioService.eliminarUsuario(eq(userId), eq(adminUsuario.getEmail()))).thenReturn(true);

        // WHEN
        String resultado = adminController.eliminarUsuario(userId, session);

        // THEN
        verify(usuarioService).eliminarUsuario(userId, adminUsuario.getEmail());
        assertEquals("redirect:/ADMIN/dashboard?success=deleted", resultado);
    }

    @Test
    void testEliminarUsuario_UsuarioAdmin_FalloEliminar() {
        // GIVEN
        Long userId = 2L;
        when(session.getAttribute("usuarioLogueado")).thenReturn(adminUsuario);
        when(usuarioService.eliminarUsuario(eq(userId), eq(adminUsuario.getEmail()))).thenReturn(false);

        // WHEN
        String resultado = adminController.eliminarUsuario(userId, session);

        // THEN
        assertEquals("redirect:/ADMIN/dashboard?error=delete_failed", resultado);
    }

    @Test
    void testEliminarUsuario_UsuarioNoAdmin_RedirigeLogin() {
        // GIVEN
        when(session.getAttribute("usuarioLogueado")).thenReturn(normalUsuario);

        // WHEN
        String resultado = adminController.eliminarUsuario(2L, session);

        // THEN
        assertEquals("redirect:/login", resultado);
        verify(usuarioService, never()).eliminarUsuario(anyLong(), anyString());
    }

    // ============================================================
    // 5. TEST GESTION DOCTORES
    // ============================================================

    @Test
    void testGestionDoctores_UsuarioAdmin_DevuelveVista() {
        // GIVEN
        when(session.getAttribute("usuarioLogueado")).thenReturn(adminUsuario);
        when(doctorService.getAllDoctores()).thenReturn(new ArrayList<>());

        // WHEN
        String resultado = adminController.gestionDoctores(session, model);

        // THEN
        assertEquals("ADMIN/gestionDoctores", resultado);
        verify(model).addAttribute(eq("doctor"), any(DoctorDTO.class));
        verify(model).addAttribute("doctores", new ArrayList<>());
        verify(model).addAttribute("usuarioLogueado", adminUsuario);
    }

    @Test
    void testGestionDoctores_UsuarioNoAdmin_RedirigeLogin() {
        // GIVEN
        when(session.getAttribute("usuarioLogueado")).thenReturn(normalUsuario);

        // WHEN
        String resultado = adminController.gestionDoctores(session, model);

        // THEN
        assertEquals("redirect:/login", resultado);
    }

    // ============================================================
    // 6. TEST AGREGAR DOCTOR
    // ============================================================

    @Test
    void testAgregarDoctor_UsuarioAdmin_Exitoso() {
        // GIVEN
        DoctorDTO doctorDTO = new DoctorDTO();
        doctorDTO.setNombre("Dr. Juan");
        doctorDTO.setApellidos("Pérez");
        doctorDTO.setEspecialidad("Cardiología");
        doctorDTO.setEmail("juan@doctor.com");

        when(session.getAttribute("usuarioLogueado")).thenReturn(adminUsuario);
        when(bindingResult.hasErrors()).thenReturn(false);

        // WHEN
        String resultado = adminController.agregarDoctor(
                doctorDTO, bindingResult, session, model, imageFile);

        // THEN
        verify(doctorService).guardarDoctor(any(Doctor.class));
        assertEquals("redirect:/ADMIN/doctores?success=created", resultado);
    }

    @Test
    void testAgregarDoctor_UsuarioAdmin_ConImagen() {
        // GIVEN
        DoctorDTO doctorDTO = new DoctorDTO();
        doctorDTO.setNombre("Dr. Juan");
        doctorDTO.setApellidos("Pérez");
        doctorDTO.setEspecialidad("Cardiología");
        doctorDTO.setEmail("juan@doctor.com");

        when(session.getAttribute("usuarioLogueado")).thenReturn(adminUsuario);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(imageFile.isEmpty()).thenReturn(false);

        // WHEN
        String resultado = adminController.agregarDoctor(
                doctorDTO, bindingResult, session, model, imageFile);

        // THEN
        verify(doctorService).guardarDoctor(any(Doctor.class), eq(imageFile));
        assertEquals("redirect:/ADMIN/doctores?success=created", resultado);
    }

    @Test
    void testAgregarDoctor_UsuarioAdmin_ErrorValidacion() {
        // GIVEN
        DoctorDTO doctorDTO = new DoctorDTO();
        when(session.getAttribute("usuarioLogueado")).thenReturn(adminUsuario);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(doctorService.getAllDoctores()).thenReturn(new ArrayList<>());

        // WHEN
        String resultado = adminController.agregarDoctor(
                doctorDTO, bindingResult, session, model, imageFile);

        // THEN
        assertEquals("ADMIN/gestionDoctores", resultado);
        verify(doctorService, never()).guardarDoctor(any(Doctor.class));
    }

    @Test
    void testAgregarDoctor_UsuarioNoAdmin_RedirigeLogin() {
        // GIVEN
        when(session.getAttribute("usuarioLogueado")).thenReturn(normalUsuario);

        // WHEN
        String resultado = adminController.agregarDoctor(
                new DoctorDTO(), bindingResult, session, model, imageFile);

        // THEN
        assertEquals("redirect:/login", resultado);
        verify(doctorService, never()).guardarDoctor(any(Doctor.class));
    }

    // ============================================================
    // 7. TEST ELIMINAR DOCTOR
    // ============================================================

    @Test
    void testEliminarDoctor_UsuarioAdmin_Exitoso() {
        // GIVEN
        Long doctorId = 1L;
        when(session.getAttribute("usuarioLogueado")).thenReturn(adminUsuario);

        // WHEN
        String resultado = adminController.eliminarDoctor(doctorId, session);

        // THEN
        verify(doctorService).eliminarDoctor(doctorId);
        assertEquals("redirect:/ADMIN/doctores?success=deleted", resultado);
    }

    @Test
    void testEliminarDoctor_UsuarioNoAdmin_RedirigeLogin() {
        // GIVEN
        when(session.getAttribute("usuarioLogueado")).thenReturn(normalUsuario);

        // WHEN
        String resultado = adminController.eliminarDoctor(1L, session);

        // THEN
        assertEquals("redirect:/login", resultado);
        verify(doctorService, never()).eliminarDoctor(anyLong());
    }

    // ============================================================
    // 8. TEST CONFIRMAR CITA
    // ============================================================

    @Test
    void testConfirmarCita_UsuarioAdmin_Exitoso() {
        // GIVEN
        Long citaId = 1L;
        Long doctorId = 1L;
        when(session.getAttribute("usuarioLogueado")).thenReturn(adminUsuario);
        when(citaService.obtenerPorId(citaId)).thenReturn(cita);
        when(doctorService.getAllDoctores()).thenReturn(Arrays.asList(doctor));

        // WHEN
        String resultado = adminController.confirmarCita(citaId, doctorId, session);

        // THEN
        verify(citaService).guardar(cita);
        assertEquals("CONFIRMADA", cita.getEstado());
        assertEquals("redirect:/ADMIN/dashboard?success=appointment_confirmed", resultado);
    }

    @Test
    void testConfirmarCita_UsuarioAdmin_CitaNoEncontrada() {
        // GIVEN
        Long citaId = 999L;
        Long doctorId = 1L;
        when(session.getAttribute("usuarioLogueado")).thenReturn(adminUsuario);
        when(citaService.obtenerPorId(citaId)).thenReturn(null);

        // WHEN
        String resultado = adminController.confirmarCita(citaId, doctorId, session);

        // THEN
        verify(citaService, never()).guardar(any(Cita.class));
        assertEquals("redirect:/ADMIN/dashboard?error=appointment_confirmation_failed", resultado);
    }

    @Test
    void testConfirmarCita_UsuarioNoAdmin_RedirigeLogin() {
        // GIVEN
        when(session.getAttribute("usuarioLogueado")).thenReturn(normalUsuario);

        // WHEN
        String resultado = adminController.confirmarCita(1L, 1L, session);

        // THEN
        assertEquals("redirect:/login", resultado);
        verify(citaService, never()).guardar(any(Cita.class));
    }

    // ============================================================
    // 9. TEST CANCELAR CITA
    // ============================================================

    @Test
    void testCancelarCita_UsuarioAdmin_Exitoso() {
        // GIVEN
        Long citaId = 1L;
        when(session.getAttribute("usuarioLogueado")).thenReturn(adminUsuario);
        when(citaService.obtenerPorId(citaId)).thenReturn(cita);

        // WHEN
        String resultado = adminController.cancelarCita(citaId, session);

        // THEN
        verify(citaService).guardar(cita);
        assertEquals("CANCELADA", cita.getEstado());
        assertEquals("redirect:/ADMIN/dashboard?success=appointment_canceled", resultado);
    }

    @Test
    void testCancelarCita_UsuarioNoAdmin_RedirigeLogin() {
        // GIVEN
        when(session.getAttribute("usuarioLogueado")).thenReturn(normalUsuario);

        // WHEN
        String resultado = adminController.cancelarCita(1L, session);

        // THEN
        assertEquals("redirect:/login", resultado);
        verify(citaService, never()).guardar(any(Cita.class));
    }

    // ============================================================
    // 10. TEST AGREGAR EXAMEN
    // ============================================================

    @Test
    void testAgregarExamen_UsuarioAdmin_Exitoso() {
        // GIVEN
        Long usuarioId = 2L;
        String tipo = "Análisis de sangre";
        String descripcion = "Hemograma completo";
        String fecha = "2026-07-15";
        
        when(session.getAttribute("usuarioLogueado")).thenReturn(adminUsuario);
        when(usuarioService.getAll()).thenReturn(Arrays.asList(adminUsuario, normalUsuario));

        // WHEN
        String resultado = adminController.agregarExamen(usuarioId, tipo, descripcion, fecha, session);

        // THEN
        verify(examenService).guardar(any(Examen.class));
        assertEquals("redirect:/ADMIN/dashboard?success=exam_added", resultado);
    }

    @Test
    void testAgregarExamen_UsuarioAdmin_UsuarioNoEncontrado() {
        // GIVEN
        Long usuarioId = 999L;
        String tipo = "Análisis";
        String descripcion = "Prueba";
        String fecha = "2026-07-15";
        
        when(session.getAttribute("usuarioLogueado")).thenReturn(adminUsuario);
        when(usuarioService.getAll()).thenReturn(Arrays.asList(adminUsuario));

        // WHEN
        String resultado = adminController.agregarExamen(usuarioId, tipo, descripcion, fecha, session);

        // THEN
        verify(examenService, never()).guardar(any(Examen.class));
        assertEquals("redirect:/ADMIN/dashboard?error=exam_add_failed", resultado);
    }

    @Test
    void testAgregarExamen_UsuarioNoAdmin_RedirigeLogin() {
        // GIVEN
        when(session.getAttribute("usuarioLogueado")).thenReturn(normalUsuario);

        // WHEN
        String resultado = adminController.agregarExamen(1L, "Tipo", "Desc", "2026-07-15", session);

        // THEN
        assertEquals("redirect:/login", resultado);
        verify(examenService, never()).guardar(any(Examen.class));
    }
}