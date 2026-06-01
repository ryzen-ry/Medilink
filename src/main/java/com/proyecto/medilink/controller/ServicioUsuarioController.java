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

        Usuario u = (Usuario) session.getAttribute("usuarioLogueado");
        if (u == null) return "redirect:/login";

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

        Usuario u = (Usuario) session.getAttribute("usuarioLogueado");
        if (u == null) return "redirect:/login";

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

        Usuario u = (Usuario) session.getAttribute("usuarioLogueado");
        if (u == null) return "redirect:/login";

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

        Usuario u = (Usuario) session.getAttribute("usuarioLogueado");
        if (u == null) return null;

        return examenService.examsPorUsuario(u);
    }
    // ------------------------------------------------------------
// SALA DE VIDEO LLAMADA
// ------------------------------------------------------------
@GetMapping("/salaVideoUser")
public String salaVideoUser(HttpSession session) {

    Usuario u = (Usuario) session.getAttribute("usuarioLogueado");
    if (u == null) return "redirect:/login";

    return "USER/salaVideoUser";  // nombre EXACTO del archivo HTML
}
@PostMapping("/guardarServicioYRedirigir")
public String guardarServicioYRedirigir(
        @RequestParam String nombre,
        @RequestParam Double precio,
        HttpSession session) {

    Usuario u = (Usuario) session.getAttribute("usuarioLogueado");
    if (u == null) return "redirect:/login";

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

    Usuario u = (Usuario) session.getAttribute("usuarioLogueado");

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




    
}
