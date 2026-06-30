package com.proyecto.medilink.controller;

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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServicioUsuarioControllerTest {

    @Mock
    private ServicioElegidoService serviciosService;

    @Mock
    private CitaService citaService;

    @Mock
    private ExamenService examenService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @InjectMocks
    private ServicioUsuarioController controller;

    private Usuario usuarioLogueado;
    private Usuario otroUsuario;
    private Rol userRol;
    private ServicioElegido servicio;
    private Cita cita;
    private Examen examen;

    @BeforeEach
    void setUp() {
        // Configurar rol
        userRol = new Rol();
        userRol.setId(1L);
        userRol.setNombre("ROLE_USER");

        // Configurar usuario logueado
        usuarioLogueado = new Usuario();
        usuarioLogueado.setId(1L);
        usuarioLogueado.setNombre("Juan Pérez");
        usuarioLogueado.setEmail("juan@email.com");
        usuarioLogueado.setRol(userRol);

        // Configurar otro usuario
        otroUsuario = new Usuario();
        otroUsuario.setId(2L);
        otroUsuario.setNombre("María López");
        otroUsuario.setEmail("maria@email.com");
        otroUsuario.setRol(userRol);

        // Configurar servicio
        servicio = new ServicioElegido();
        servicio.setId(1L);
        servicio.setNombreServicio("Consulta general");
        servicio.setPrecio(50.0);
        servicio.setUsuario(usuarioLogueado);

        // Configurar cita
        cita = new Cita();
        cita.setId(1L);
        cita.setMotivo("Dolor de cabeza");
        cita.setFecha(LocalDate.now().plusDays(5));
        cita.setEstado("PENDIENTE");
        cita.setUsuario(usuarioLogueado);

        // Configurar examen
        examen = new Examen();
        examen.setId(1L);
        examen.setTipo("Análisis de sangre");
        examen.setDescripcion("Hemograma completo");
        examen.setFecha(LocalDate.now());
        examen.setUsuario(usuarioLogueado);
    }

    // ============================================================
    // 1. TEST SERVICIOS USER (VISTA PRINCIPAL)
    // ============================================================

    @Test
    void testServiciosUser_UsuarioLogueado_DevuelveVista() {
        when(session.getAttribute("usuarioLogueado")).thenReturn(usuarioLogueado);
        when(serviciosService.listarPorUsuario(usuarioLogueado)).thenReturn(Arrays.asList(servicio));
        when(citaService.listarPorUsuario(usuarioLogueado)).thenReturn(Arrays.asList(cita));
        when(examenService.examsPorUsuario(usuarioLogueado)).thenReturn(Arrays.asList(examen));

        String resultado = controller.serviciosUser(model, session);

        assertEquals("USER/serviciosUser", resultado);
        verify(model).addAttribute("servicios", Arrays.asList(servicio));
        verify(model).addAttribute("citas", Arrays.asList(cita));
        verify(model).addAttribute("examenes", Arrays.asList(examen));
    }

    @Test
    void testServiciosUser_UsuarioNoLogueado_RedirigeLogin() {
        when(session.getAttribute("usuarioLogueado")).thenReturn(null);

        String resultado = controller.serviciosUser(model, session);

        assertEquals("redirect:/login", resultado);
        verify(model, never()).addAttribute(anyString(), any());
    }

    // ============================================================
    // 2. TEST GUARDAR SERVICIO
    // ============================================================

    @Test
    void testGuardarServicio_UsuarioLogueado_Exitoso() {
        String nombre = "Consulta general";
        Double precio = 50.0;

        when(session.getAttribute("usuarioLogueado")).thenReturn(usuarioLogueado);
        when(serviciosService.guardar(any(ServicioElegido.class))).thenReturn(servicio);

        String resultado = controller.guardarServicio(nombre, precio, session);

        verify(serviciosService).guardar(any(ServicioElegido.class));
        assertEquals("redirect:/USER/serviciosUser?successServicio", resultado);
    }

    @Test
    void testGuardarServicio_UsuarioNoLogueado_RedirigeLogin() {
        when(session.getAttribute("usuarioLogueado")).thenReturn(null);

        String resultado = controller.guardarServicio("Consulta", 50.0, session);

        assertEquals("redirect:/login", resultado);
        verify(serviciosService, never()).guardar(any(ServicioElegido.class));
    }

    // ============================================================
    // 3. TEST AGENDAR CITA
    // ============================================================

    @Test
    void testAgendarCita_UsuarioLogueado_Exitoso() {
        String motivo = "Dolor de cabeza";
        String fecha = LocalDate.now().plusDays(5).toString();

        when(session.getAttribute("usuarioLogueado")).thenReturn(usuarioLogueado);
        when(citaService.guardar(any(Cita.class))).thenReturn(cita);

        String resultado = controller.agendarCita(motivo, fecha, session);

        verify(citaService).guardar(any(Cita.class));
        assertEquals("redirect:/USER/serviciosUser?successCita", resultado);
    }

    @Test
    void testAgendarCita_UsuarioNoLogueado_RedirigeLogin() {
        when(session.getAttribute("usuarioLogueado")).thenReturn(null);

        String resultado = controller.agendarCita("Motivo", "2026-07-15", session);

        assertEquals("redirect:/login", resultado);
        verify(citaService, never()).guardar(any(Cita.class));
    }

    // ============================================================
    // 4. TEST HISTORIAL AJAX
    // ============================================================

    @Test
    void testHistorialAjax_UsuarioLogueado_DevuelveExamenes() {
        when(session.getAttribute("usuarioLogueado")).thenReturn(usuarioLogueado);
        when(examenService.examsPorUsuario(usuarioLogueado)).thenReturn(Arrays.asList(examen));

        Object resultado = controller.historialAjax(session);

        assertNotNull(resultado);
        assertEquals(Arrays.asList(examen), resultado);
        verify(examenService).examsPorUsuario(usuarioLogueado);
    }

    @Test
    void testHistorialAjax_UsuarioNoLogueado_DevuelveNull() {
        when(session.getAttribute("usuarioLogueado")).thenReturn(null);

        Object resultado = controller.historialAjax(session);

        assertNull(resultado);
        verify(examenService, never()).examsPorUsuario(any());
    }

    // ============================================================
    // 5. TEST SALA VIDEO
    // ============================================================

    @Test
    void testSalaVideoUser_UsuarioLogueado_DevuelveVista() {
        when(session.getAttribute("usuarioLogueado")).thenReturn(usuarioLogueado);

        String resultado = controller.salaVideoUser(session);

        assertEquals("USER/salaVideoUser", resultado);
    }

    @Test
    void testSalaVideoUser_UsuarioNoLogueado_RedirigeLogin() {
        when(session.getAttribute("usuarioLogueado")).thenReturn(null);

        String resultado = controller.salaVideoUser(session);

        assertEquals("redirect:/login", resultado);
    }

    // ============================================================
    // 6. TEST GUARDAR SERVICIO Y REDIRIGIR
    // ============================================================

    @Test
    void testGuardarServicioYRedirigir_UsuarioLogueado_Exitoso() {
        String nombre = "Videoconsulta";
        Double precio = 50.0;

        when(session.getAttribute("usuarioLogueado")).thenReturn(usuarioLogueado);
        when(serviciosService.guardar(any(ServicioElegido.class))).thenReturn(servicio);

        String resultado = controller.guardarServicioYRedirigir(nombre, precio, session);

        verify(serviciosService).guardar(any(ServicioElegido.class));
        assertEquals("redirect:/USER/salaVideoUser", resultado);
    }

    @Test
    void testGuardarServicioYRedirigir_UsuarioNoLogueado_RedirigeLogin() {
        when(session.getAttribute("usuarioLogueado")).thenReturn(null);

        String resultado = controller.guardarServicioYRedirigir("Videoconsulta", 50.0, session);

        assertEquals("redirect:/login", resultado);
        verify(serviciosService, never()).guardar(any(ServicioElegido.class));
    }

    // ============================================================
    // 7. TEST GUARDAR CITA FORMULARIO
    // ============================================================

    @Test
    void testGuardarCitaFormulario_UsuarioLogueado_Exitoso() {
        String nombre = "Juan Pérez";
        String email = "juan@email.com";
        String telefono = "999999999";
        String fechaNacimiento = "1990-01-15";
        String especialidad = "Cardiología";
        String fechaPreferida = LocalDate.now().plusDays(5).toString();
        String motivo = "Dolor en el pecho";

        when(session.getAttribute("usuarioLogueado")).thenReturn(usuarioLogueado);
        when(citaService.guardar(any(Cita.class))).thenReturn(cita);

        String resultado = controller.guardarCitaFormulario(
                nombre, email, telefono, fechaNacimiento, especialidad,
                fechaPreferida, motivo, session);

        verify(citaService).guardar(any(Cita.class));
        assertEquals("redirect:/USER/citasUser?success", resultado);
    }

    @Test
    void testGuardarCitaFormulario_UsuarioNoLogueado_SinUsuario() {
        String nombre = "Juan Pérez";
        String email = "juan@email.com";
        String telefono = "999999999";
        String especialidad = "Cardiología";
        String fechaPreferida = LocalDate.now().plusDays(5).toString();
        String motivo = "Dolor en el pecho";

        when(session.getAttribute("usuarioLogueado")).thenReturn(null);
        when(citaService.guardar(any(Cita.class))).thenReturn(cita);

        String resultado = controller.guardarCitaFormulario(
                nombre, email, telefono, null, especialidad,
                fechaPreferida, motivo, session);

        verify(citaService).guardar(any(Cita.class));
        assertEquals("redirect:/USER/citasUser?success", resultado);
    }

    @Test
    void testGuardarCitaFormulario_ConFechaNacimientoNula() {
        String nombre = "Juan Pérez";
        String email = "juan@email.com";
        String telefono = "999999999";
        String especialidad = "Cardiología";
        String fechaPreferida = LocalDate.now().plusDays(5).toString();
        String motivo = "Dolor en el pecho";

        when(session.getAttribute("usuarioLogueado")).thenReturn(usuarioLogueado);
        when(citaService.guardar(any(Cita.class))).thenReturn(cita);

        String resultado = controller.guardarCitaFormulario(
                nombre, email, telefono, null, especialidad,
                fechaPreferida, motivo, session);

        verify(citaService).guardar(any(Cita.class));
        assertEquals("redirect:/USER/citasUser?success", resultado);
    }

    // ============================================================
    // 8. TEST CANCELAR CITA
    // ============================================================

    @Test
    void testCancelarCitaUsuario_UsuarioPropietario_Exitoso() {
        Long citaId = 1L;

        when(session.getAttribute("usuarioLogueado")).thenReturn(usuarioLogueado);
        when(citaService.obtenerPorId(citaId)).thenReturn(cita);

        String resultado = controller.cancelarCitaUsuario(citaId, session);

        verify(citaService).guardar(cita);
        assertEquals("CANCELADA", cita.getEstado());
        assertEquals("redirect:/USER/serviciosUser?successCancelCita", resultado);
    }

    @Test
    void testCancelarCitaUsuario_UsuarioNoPropietario_Error() {
        Long citaId = 1L;
        // El usuario logueado es otro (no el propietario de la cita)
        Usuario otroUsuarioLogueado = new Usuario();
        otroUsuarioLogueado.setId(99L);

        when(session.getAttribute("usuarioLogueado")).thenReturn(otroUsuarioLogueado);
        when(citaService.obtenerPorId(citaId)).thenReturn(cita);

        String resultado = controller.cancelarCitaUsuario(citaId, session);

        verify(citaService, never()).guardar(any(Cita.class));
        assertEquals("redirect:/USER/serviciosUser?errorCancelCita", resultado);
    }

    @Test
    void testCancelarCitaUsuario_CitaNoPendiente_Error() {
        Long citaId = 1L;
        cita.setEstado("CONFIRMADA"); // No es PENDIENTE

        when(session.getAttribute("usuarioLogueado")).thenReturn(usuarioLogueado);
        when(citaService.obtenerPorId(citaId)).thenReturn(cita);

        String resultado = controller.cancelarCitaUsuario(citaId, session);

        verify(citaService, never()).guardar(any(Cita.class));
        assertEquals("redirect:/USER/serviciosUser?errorCancelCita", resultado);
    }

    @Test
    void testCancelarCitaUsuario_CitaNoEncontrada_Error() {
        Long citaId = 999L;

        when(session.getAttribute("usuarioLogueado")).thenReturn(usuarioLogueado);
        when(citaService.obtenerPorId(citaId)).thenReturn(null);

        String resultado = controller.cancelarCitaUsuario(citaId, session);

        verify(citaService, never()).guardar(any(Cita.class));
        assertEquals("redirect:/USER/serviciosUser?errorCancelCita", resultado);
    }

    @Test
    void testCancelarCitaUsuario_UsuarioNoLogueado_RedirigeLogin() {
        when(session.getAttribute("usuarioLogueado")).thenReturn(null);

        String resultado = controller.cancelarCitaUsuario(1L, session);

        assertEquals("redirect:/login", resultado);
        verify(citaService, never()).guardar(any(Cita.class));
    }
}
