package com.proyecto.medilink.controller;

import com.proyecto.medilink.model.*;
import com.proyecto.medilink.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/USER")
public class ServicioUsuarioController {

    private static final String USUARIO_LOGUEADO = "usuarioLogueado";
    private static final String REDIRECT_LOGIN = "redirect:/login";

    @Autowired
    private ServicioElegidoService serviciosService;

    @Autowired
    private CitaService citaService;

    @Autowired
    private ExamenService examenService;

    // ------------------------------------------------------------
    // VISTA PRINCIPAL (Tarjetas: Servicios - Citas - Historial modal)
    // ------------------------------------------------------------
    @GetMapping("/serviciosUser")
    public String serviciosUser(Model model, HttpSession session) {

        Usuario u = (Usuario) session.getAttribute(USUARIO_LOGUEADO);
        if (u == null) return REDIRECT_LOGIN;

        model.addAttribute("servicios", serviciosService.listarPorUsuario(u));
        model.addAttribute("citas", citaService.listarPorUsuario(u));
        model.addAttribute("examenes", examenService.examsPorUsuario(u));

        return "USER/serviciosUser";
    }

    // ------------------------------------------------------------
    // GUARDAR SERVICIO
    // ------------------------------------------------------------
    @PostMapping("/guardarServicio")
    public String guardarServicio(@RequestParam String nombre,
                                  @RequestParam Double precio,
                                  HttpSession session) {

        Usuario u = (Usuario) session.getAttribute(USUARIO_LOGUEADO);
        if (u == null) return REDIRECT_LOGIN;

        ServicioElegido s = new ServicioElegido();
        s.setNombreServicio(nombre);
        s.setPrecio(precio);
        s.setUsuario(u);

        serviciosService.guardar(s);

        return "redirect:/USER/serviciosUser?successServicio";
    }

    // ------------------------------------------------------------
    // AGENDAR CITA DESDE MODAL
    // ------------------------------------------------------------
    @PostMapping("/agendarCita")
    public String agendarCita(@RequestParam String motivo,
                              @RequestParam String fecha,
                              HttpSession session) {

        Usuario u = (Usuario) session.getAttribute(USUARIO_LOGUEADO);
        if (u == null) return REDIRECT_LOGIN;

        Cita c = new Cita();
        c.setMotivo(motivo);
        c.setFecha(java.time.LocalDate.parse(fecha));
        c.setUsuario(u);

        citaService.guardar(c);

        return "redirect:/USER/serviciosUser?successCita";
    }

    // ------------------------------------------------------------
    // HISTORIAL AJAX (opcional)
    // ------------------------------------------------------------
    @GetMapping("/historial")
    @ResponseBody
    public Object historialAjax(HttpSession session) {

        Usuario u = (Usuario) session.getAttribute(USUARIO_LOGUEADO);
        if (u == null) return null;

        return examenService.examsPorUsuario(u);
    }
    // ------------------------------------------------------------
// SALA DE VIDEO LLAMADA
// ------------------------------------------------------------
@GetMapping("/salaVideoUser")
public String salaVideoUser(HttpSession session) {

    Usuario u = (Usuario) session.getAttribute(USUARIO_LOGUEADO);
    if (u == null) return REDIRECT_LOGIN;

    return "USER/salaVideoUser";  // nombre EXACTO del archivo HTML
}
@PostMapping("/guardarServicioYRedirigir")
public String guardarServicioYRedirigir(
        @RequestParam String nombre,
        @RequestParam Double precio,
        HttpSession session) {

    Usuario u = (Usuario) session.getAttribute(USUARIO_LOGUEADO);
    if (u == null) return REDIRECT_LOGIN;

    ServicioElegido s = new ServicioElegido();
    s.setNombreServicio("Videoconsulta - " + nombre);  // 👈 AQUÍ LA MAGIA
    s.setPrecio(precio);
    s.setUsuario(u);

    serviciosService.guardar(s);

    return "redirect:/USER/salaVideoUser";
}
@PostMapping("/guardarCitaFormulario")
public String guardarCitaFormulario(
        @RequestParam("nombre") String nombre,
        @RequestParam("email") String email,
        @RequestParam("telefono") String telefono,
        @RequestParam(value = "fecha_nacimiento", required = false) String fechaNacimientoStr,
        @RequestParam("especialidad") String especialidad,
        @RequestParam("fecha_preferida") String fechaPreferidaStr,
        @RequestParam("motivo") String motivo,
        HttpSession session) {

    Usuario u = (Usuario) session.getAttribute(USUARIO_LOGUEADO);

    Cita cita = new Cita();

    // Datos del formulario
    cita.setNombrePaciente(nombre);
    cita.setEmailPaciente(email);
    cita.setTelefonoPaciente(telefono);

    if (fechaNacimientoStr != null && !fechaNacimientoStr.isBlank()) {
        cita.setFechaNacimiento(java.time.LocalDate.parse(fechaNacimientoStr));
    }

    cita.setEspecialidad(especialidad);

    // fecha_preferida -> fecha en Cita
    cita.setFecha(java.time.LocalDate.parse(fechaPreferidaStr));

    cita.setMotivo(motivo);

    // Si el usuario está logueado, enlazamos la cita al usuario
    if (u != null) {
        cita.setUsuario(u);
    }

    // Opcional: estado por defecto y createdAt se manejan en @PrePersist
    citaService.guardar(cita);

    return "redirect:/USER/citasUser?success";
}

// ✅ Cancelar cita por el propio usuario (si está PENDIENTE)
@PostMapping("/citas/cancelar/{id}")
public String cancelarCitaUsuario(@PathVariable Long id, HttpSession session) {
    Usuario u = (Usuario) session.getAttribute(USUARIO_LOGUEADO);
    if (u == null) return REDIRECT_LOGIN;

    Cita cita = citaService.obtenerPorId(id);
    if (cita != null && cita.getUsuario().getId().equals(u.getId()) && "PENDIENTE".equals(cita.getEstado())) {
        cita.setEstado("CANCELADA");
        citaService.guardar(cita);
        return "redirect:/USER/serviciosUser?successCancelCita";
    }

    return "redirect:/USER/serviciosUser?errorCancelCita";
}

}