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
    private Rol userRol;
    private Doctor doctor;
    private Cita cita;
    private Examen examen;

    @BeforeEach
    void setUp() {
        // Configurar rol admin
        adminRol = new Rol();
        adminRol.setId(1L);
        adminRol.setNombre("ROLE_ADMIN");

        // Configurar rol user
        userRol = new Rol();
        userRol.setId(2L);
        userRol.setNombre("ROLE_USER");

        // Configurar usuario admin
        adminUsuario = new Usuario();
        adminUsuario.setId(1L);
        adminUsuario.setNombre("Administrador");
        adminUsuario.setEmail("admin@admin.com");
        adminUsuario.setRol(adminRol);

        // Configurar usuario normal
        normalUsuario = new Usuario();
        normalUsuario.setId(2L);
        normalUsuario.setNombre("Usuario Normal");
        normalUsuario.setEmail("user@user.com");
        normalUsuario.setRol(userRol);

        // Configurar doctor
        doctor = new Doctor();
        doctor.setId(1L);
        doctor.setNombre("Dr. Juan");
        doctor.setApellidos("Pérez");
        doctor.setEspecialidad("Cardiología");
        doctor.setEmail("juan@doctor.com");

        // Configurar cita
        cita = new Cita();
        cita.setId(1L);
        cita.setMotivo("Consulta general");
        cita.setEstado("PENDIENTE");
        cita.setUsuario(normalUsuario);

        // Configurar examen
        examen = new Examen();
        examen.setId(1L);
        examen.setTipo("Análisis de sangre");
        examen.setDescripcion("Hemograma completo");
        examen.setFecha(LocalDate.now());
    }

    // ============================================================
    // 1. TEST DASHBOARD
    // ============================================================

    @Test
    void testDashboard_UsuarioAdmin_DevuelveDashboard() {
        when(session.getAttribute("usuarioLogueado")).thenReturn(adminUsuario);
        when(usuarioService.getAll()).thenReturn(new ArrayList<>());
        when(doctorService.getAllDoctores()).thenReturn(new ArrayList<>());
        when(citaService.listarTodas()).thenReturn(new ArrayList<>());

        String resultado = adminController.dashboard(session, model);

        assertEquals("ADMIN/dashboard", resultado);
        verify(model).addAttribute("usuarioLogueado", adminUsuario);
        verify(model).addAttribute("usuarios", new ArrayList<>());
        verify(model).addAttribute("doctores", new ArrayList<>());
        verify(model).addAttribute("citas", new ArrayList<>());
    }

    @Test
    void testDashboard_UsuarioNoAdmin_RedirigeLogin() {
        when(session.getAttribute("usuarioLogueado")).thenReturn(normalUsuario);

        String resultado = adminController.dashboard(session, model);

        assertEquals("redirect:/login", resultado);
        verify(model, never()).addAttribute(anyString(), any());
    }

    @Test
    void testDashboard_SinSesion_RedirigeLogin() {
        when(session.getAttribute("usuarioLogueado")).thenReturn(null);

        String resultado = adminController.dashboard(session, model);

        assertEquals("redirect:/login", resultado);
    }

    // ============================================================
    // 2. TEST LISTAR USUARIOS
    // ============================================================

    @Test
    void testListarUsuarios_UsuarioAdmin_RedirigeDashboard() {
        when(session.getAttribute("usuarioLogueado")).thenReturn(adminUsuario);

        String resultado = adminController.listarUsuarios(session, model);

        assertEquals("redirect:/ADMIN/dashboard", resultado);
    }

    @Test
    void testListarUsuarios_UsuarioNoAdmin_RedirigeLogin() {
        when(session.getAttribute("usuarioLogueado")).thenReturn(normalUsuario);

        String resultado = adminController.listarUsuarios(session, model);

        assertEquals("redirect:/login", resultado);
    }

    // ============================================================
    // 3. TEST CAMBIAR ROL
    // ============================================================

    @Test
    void testCambiarRol_UsuarioAdmin_CambiaRol() {
        Long userId = 2L;
        String nuevoRol = "ROLE_USER";
        when(session.getAttribute("usuarioLogueado")).thenReturn(adminUsuario);

        String resultado = adminController.cambiarRol(userId, nuevoRol, session);

        verify(usuarioService).cambiarRol(userId, nuevoRol);
        assertEquals("redirect:/ADMIN/usuarios", resultado);
    }

    @Test
    void testCambiarRol_UsuarioNoAdmin_RedirigeLogin() {
        when(session.getAttribute("usuarioLogueado")).thenReturn(normalUsuario);

        String resultado = adminController.cambiarRol(2L, "ROLE_USER", session);

        assertEquals("redirect:/login", resultado);
        verify(usuarioService, never()).cambiarRol(anyLong(), anyString());
    }

    // ============================================================
    // 4. TEST ELIMINAR USUARIO
    // ============================================================

    @Test
    void testEliminarUsuario_UsuarioAdmin_EliminaExitoso() {
        Long userId = 2L;
        when(session.getAttribute("usuarioLogueado")).thenReturn(adminUsuario);
        when(usuarioService.eliminarUsuario(eq(userId), eq(adminUsuario.getEmail()))).thenReturn(true);

        String resultado = adminController.eliminarUsuario(userId, session);

        verify(usuarioService).eliminarUsuario(userId, adminUsuario.getEmail());
        assertEquals("redirect:/ADMIN/dashboard?success=deleted", resultado);
    }

    @Test
    void testEliminarUsuario_UsuarioAdmin_FalloEliminar() {
        Long userId = 2L;
        when(session.getAttribute("usuarioLogueado")).thenReturn(adminUsuario);
        when(usuarioService.eliminarUsuario(eq(userId), eq(adminUsuario.getEmail()))).thenReturn(false);

        String resultado = adminController.eliminarUsuario(userId, session);

        assertEquals("redirect:/ADMIN/dashboard?error=delete_failed", resultado);
    }

    @Test
    void testEliminarUsuario_UsuarioNoAdmin_RedirigeLogin() {
        when(session.getAttribute("usuarioLogueado")).thenReturn(normalUsuario);

        String resultado = adminController.eliminarUsuario(2L, session);

        assertEquals("redirect:/login", resultado);
        verify(usuarioService, never()).eliminarUsuario(anyLong(), anyString());
    }

    @Test
    void testEliminarUsuario_UsuarioAdmin_LanzaExcepcion() {
        Long userId = 2L;
        when(session.getAttribute("usuarioLogueado")).thenReturn(adminUsuario);
        when(usuarioService.eliminarUsuario(eq(userId), eq(adminUsuario.getEmail())))
                .thenThrow(new RuntimeException("Error de base de datos"));

        String resultado = adminController.eliminarUsuario(userId, session);

        assertEquals("redirect:/ADMIN/usuarios?error=exception", resultado);
    }

    // ============================================================
    // 5. TEST GESTION DOCTORES
    // ============================================================

    @Test
    void testGestionDoctores_UsuarioAdmin_DevuelveVista() {
        when(session.getAttribute("usuarioLogueado")).thenReturn(adminUsuario);
        when(doctorService.getAllDoctores()).thenReturn(new ArrayList<>());

        String resultado = adminController.gestionDoctores(session, model);

        assertEquals("ADMIN/gestionDoctores", resultado);
        verify(model).addAttribute(eq("doctor"), any(DoctorDTO.class));
        verify(model).addAttribute("doctores", new ArrayList<>());
        verify(model).addAttribute("usuarioLogueado", adminUsuario);
    }

    @Test
    void testGestionDoctores_UsuarioNoAdmin_RedirigeLogin() {
        when(session.getAttribute("usuarioLogueado")).thenReturn(normalUsuario);

        String resultado = adminController.gestionDoctores(session, model);

        assertEquals("redirect:/login", resultado);
    }

    // ============================================================
    // 6. TEST AGREGAR DOCTOR
    // ============================================================

    @Test
    void testAgregarDoctor_UsuarioAdmin_Exitoso() {
        DoctorDTO doctorDTO = crearDoctorDTO();

        when(session.getAttribute("usuarioLogueado")).thenReturn(adminUsuario);
        when(bindingResult.hasErrors()).thenReturn(false);

        String resultado = adminController.agregarDoctor(
                doctorDTO, bindingResult, session, model, imageFile);

        verify(doctorService).guardarDoctor(any(Doctor.class), any(MultipartFile.class));
        assertEquals("redirect:/ADMIN/doctores?success=created", resultado);
    }

    @Test
    void testAgregarDoctor_UsuarioAdmin_ConImagen() {
        DoctorDTO doctorDTO = crearDoctorDTO();

        when(session.getAttribute("usuarioLogueado")).thenReturn(adminUsuario);
        when(bindingResult.hasErrors()).thenReturn(false);
        // No stub necesarios para imageFile

        String resultado = adminController.agregarDoctor(
                doctorDTO, bindingResult, session, model, imageFile);

        verify(doctorService).guardarDoctor(any(Doctor.class), eq(imageFile));
        assertEquals("redirect:/ADMIN/doctores?success=created", resultado);
    }

    @Test
    void testAgregarDoctor_UsuarioAdmin_ErrorValidacion() {
        DoctorDTO doctorDTO = new DoctorDTO();
        when(session.getAttribute("usuarioLogueado")).thenReturn(adminUsuario);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(doctorService.getAllDoctores()).thenReturn(new ArrayList<>());

        String resultado = adminController.agregarDoctor(
                doctorDTO, bindingResult, session, model, imageFile);

        assertEquals("ADMIN/gestionDoctores", resultado);
        verify(doctorService, never()).guardarDoctor(any(Doctor.class), any(MultipartFile.class));
    }

    @Test
    void testAgregarDoctor_UsuarioNoAdmin_RedirigeLogin() {
        when(session.getAttribute("usuarioLogueado")).thenReturn(normalUsuario);

        String resultado = adminController.agregarDoctor(
                new DoctorDTO(), bindingResult, session, model, imageFile);

        assertEquals("redirect:/login", resultado);
        verify(doctorService, never()).guardarDoctor(any(Doctor.class), any(MultipartFile.class));
    }

    @Test
    void testAgregarDoctor_UsuarioAdmin_LanzaExcepcion() {
        DoctorDTO doctorDTO = crearDoctorDTO();

        when(session.getAttribute("usuarioLogueado")).thenReturn(adminUsuario);
        when(bindingResult.hasErrors()).thenReturn(false);
        doThrow(new RuntimeException("Email duplicado"))
                .when(doctorService).guardarDoctor(any(Doctor.class), any(MultipartFile.class));
        when(doctorService.getAllDoctores()).thenReturn(new ArrayList<>());

        String resultado = adminController.agregarDoctor(
                doctorDTO, bindingResult, session, model, imageFile);

        assertEquals("ADMIN/gestionDoctores", resultado);
        verify(model).addAttribute(eq("error"), anyString());
    }

    // ============================================================
    // 7. TEST ELIMINAR DOCTOR
    // ============================================================

    @Test
    void testEliminarDoctor_UsuarioAdmin_Exitoso() {
        Long doctorId = 1L;
        when(session.getAttribute("usuarioLogueado")).thenReturn(adminUsuario);

        String resultado = adminController.eliminarDoctor(doctorId, session);

        verify(doctorService).eliminarDoctor(doctorId);
        assertEquals("redirect:/ADMIN/doctores?success=deleted", resultado);
    }

    @Test
    void testEliminarDoctor_UsuarioNoAdmin_RedirigeLogin() {
        when(session.getAttribute("usuarioLogueado")).thenReturn(normalUsuario);

        String resultado = adminController.eliminarDoctor(1L, session);

        assertEquals("redirect:/login", resultado);
        verify(doctorService, never()).eliminarDoctor(anyLong());
    }

    @Test
    void testEliminarDoctor_UsuarioAdmin_LanzaExcepcion() {
        Long doctorId = 1L;
        when(session.getAttribute("usuarioLogueado")).thenReturn(adminUsuario);
        doThrow(new RuntimeException("Error al eliminar")).when(doctorService).eliminarDoctor(doctorId);

        String resultado = adminController.eliminarDoctor(doctorId, session);

        assertEquals("redirect:/ADMIN/doctores?error=delete_failed", resultado);
    }

    // ============================================================
    // 8. TEST CONFIRMAR CITA
    // ============================================================

    @Test
    void testConfirmarCita_UsuarioAdmin_Exitoso() {
        Long citaId = 1L;
        Long doctorId = 1L;
        when(session.getAttribute("usuarioLogueado")).thenReturn(adminUsuario);
        when(citaService.obtenerPorId(citaId)).thenReturn(cita);
        when(doctorService.getAllDoctores()).thenReturn(Arrays.asList(doctor));

        String resultado = adminController.confirmarCita(citaId, doctorId, session);

        verify(citaService).guardar(cita);
        assertEquals("CONFIRMADA", cita.getEstado());
        assertEquals("redirect:/ADMIN/dashboard?success=appointment_confirmed", resultado);
    }

    @Test
    void testConfirmarCita_UsuarioAdmin_CitaNoEncontrada() {
        Long citaId = 999L;
        Long doctorId = 1L;
        when(session.getAttribute("usuarioLogueado")).thenReturn(adminUsuario);
        when(citaService.obtenerPorId(citaId)).thenReturn(null);

        String resultado = adminController.confirmarCita(citaId, doctorId, session);

        verify(citaService, never()).guardar(any(Cita.class));
        assertEquals("redirect:/ADMIN/dashboard?error=appointment_confirmation_failed", resultado);
    }

    @Test
    void testConfirmarCita_UsuarioNoAdmin_RedirigeLogin() {
        when(session.getAttribute("usuarioLogueado")).thenReturn(normalUsuario);

        String resultado = adminController.confirmarCita(1L, 1L, session);

        assertEquals("redirect:/login", resultado);
        verify(citaService, never()).guardar(any(Cita.class));
    }

    // ============================================================
    // 9. TEST CANCELAR CITA
    // ============================================================

    @Test
    void testCancelarCita_UsuarioAdmin_Exitoso() {
        Long citaId = 1L;
        when(session.getAttribute("usuarioLogueado")).thenReturn(adminUsuario);
        when(citaService.obtenerPorId(citaId)).thenReturn(cita);

        String resultado = adminController.cancelarCita(citaId, session);

        verify(citaService).guardar(cita);
        assertEquals("CANCELADA", cita.getEstado());
        assertEquals("redirect:/ADMIN/dashboard?success=appointment_canceled", resultado);
    }

    @Test
    void testCancelarCita_UsuarioAdmin_CitaNoEncontrada() {
        Long citaId = 999L;
        when(session.getAttribute("usuarioLogueado")).thenReturn(adminUsuario);
        when(citaService.obtenerPorId(citaId)).thenReturn(null);

        String resultado = adminController.cancelarCita(citaId, session);

        verify(citaService, never()).guardar(any(Cita.class));
        assertEquals("redirect:/ADMIN/dashboard?error=appointment_cancelation_failed", resultado);
    }

    @Test
    void testCancelarCita_UsuarioNoAdmin_RedirigeLogin() {
        when(session.getAttribute("usuarioLogueado")).thenReturn(normalUsuario);

        String resultado = adminController.cancelarCita(1L, session);

        assertEquals("redirect:/login", resultado);
        verify(citaService, never()).guardar(any(Cita.class));
    }

    // ============================================================
    // 10. TEST AGREGAR EXAMEN
    // ============================================================

    @Test
    void testAgregarExamen_UsuarioAdmin_Exitoso() {
        Long usuarioId = 2L;
        String tipo = "Análisis de sangre";
        String descripcion = "Hemograma completo";
        String fecha = "2026-07-15";
        
        when(session.getAttribute("usuarioLogueado")).thenReturn(adminUsuario);
        when(usuarioService.getAll()).thenReturn(Arrays.asList(adminUsuario, normalUsuario));

        String resultado = adminController.agregarExamen(usuarioId, tipo, descripcion, fecha, session);

        verify(examenService).guardar(any(Examen.class));
        assertEquals("redirect:/ADMIN/dashboard?success=exam_added", resultado);
    }

    @Test
    void testAgregarExamen_UsuarioAdmin_UsuarioNoEncontrado() {
        Long usuarioId = 999L;
        String tipo = "Análisis";
        String descripcion = "Prueba";
        String fecha = "2026-07-15";
        
        when(session.getAttribute("usuarioLogueado")).thenReturn(adminUsuario);
        when(usuarioService.getAll()).thenReturn(Arrays.asList(adminUsuario));

        String resultado = adminController.agregarExamen(usuarioId, tipo, descripcion, fecha, session);

        verify(examenService, never()).guardar(any(Examen.class));
        assertEquals("redirect:/ADMIN/dashboard?error=exam_add_failed", resultado);
    }

    @Test
    void testAgregarExamen_UsuarioNoAdmin_RedirigeLogin() {
        when(session.getAttribute("usuarioLogueado")).thenReturn(normalUsuario);

        String resultado = adminController.agregarExamen(1L, "Tipo", "Desc", "2026-07-15", session);

        assertEquals("redirect:/login", resultado);
        verify(examenService, never()).guardar(any(Examen.class));
    }

    // ============================================================
    // MÉTODO AUXILIAR
    // ============================================================

    private DoctorDTO crearDoctorDTO() {
        DoctorDTO dto = new DoctorDTO();
        dto.setNombre("Dr. Juan");
        dto.setApellidos("Pérez");
        dto.setEspecialidad("Cardiología");
        dto.setEmail("juan@doctor.com");
        return dto;
    }
}
