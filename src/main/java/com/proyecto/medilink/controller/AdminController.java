package com.proyecto.medilink.controller;

import com.proyecto.medilink.model.Doctor;
import com.proyecto.medilink.model.Usuario;
import com.proyecto.medilink.model.Cita;
import com.proyecto.medilink.model.Examen;
import com.proyecto.medilink.service.DoctorService;
import com.proyecto.medilink.service.UsuarioService;
import com.proyecto.medilink.service.CitaService;
import com.proyecto.medilink.service.ExamenService;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@Controller
@RequestMapping("/ADMIN")
public class AdminController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private CitaService citaService;

    @Autowired
    private ExamenService examenService;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Usuario u = (Usuario) session.getAttribute("usuarioLogueado");

        if (u == null || !u.getRol().getNombre().equals("ROLE_ADMIN")) {
            return "redirect:/login";
        }

        model.addAttribute("usuarioLogueado", u);
        model.addAttribute("usuarios", usuarioService.getAll());
        model.addAttribute("doctores", doctorService.getAllDoctores()); // Añadir lista de doctores
        model.addAttribute("citas", citaService.listarTodas()); // Añadir lista de citas

        return "ADMIN/dashboard";
    }


    @GetMapping("/usuarios")
    public String listarUsuarios(HttpSession session, Model model) {
        Usuario u = (Usuario) session.getAttribute("usuarioLogueado");

        if (u == null || !u.getRol().getNombre().equals("ROLE_ADMIN")) {
            return "redirect:/login";
        }

        // Redirigir al dashboard que ya muestra la lista de usuarios
        return "redirect:/ADMIN/dashboard";
    }

    @PostMapping("/cambiar-rol/{id}")
    public String cambiarRol(@PathVariable Long id,
                             @RequestParam String nuevoRol,
                             HttpSession session) {

        Usuario u = (Usuario) session.getAttribute("usuarioLogueado");

        if (u == null || !u.getRol().getNombre().equals("ROLE_ADMIN")) {
            return "redirect:/login";
        }

        usuarioService.cambiarRol(id, nuevoRol);
        return "redirect:/ADMIN/usuarios";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id,
                                  HttpSession session) {

        Usuario adminActual = (Usuario) session.getAttribute("usuarioLogueado");

        if (adminActual == null || !adminActual.getRol().getNombre().equals("ROLE_ADMIN")) {
            return "redirect:/login";
        }

        try {
            boolean deleted = usuarioService.eliminarUsuario(id, adminActual.getEmail());
            if (!deleted) {
                return "redirect:/ADMIN/dashboard?error=delete_failed";
            }
            return "redirect:/ADMIN/dashboard?success=deleted";
        } catch (Exception e) {
            // Evitar exponer stacktrace al usuario; redirigir con flag de error
            return "redirect:/ADMIN/usuarios?error=exception";
        }
    }

    @GetMapping("/doctores")
    public String gestionDoctores(HttpSession session, Model model) {
        Usuario u = (Usuario) session.getAttribute("usuarioLogueado");
        if (u == null || !u.getRol().getNombre().equals("ROLE_ADMIN")) {
            return "redirect:/login";
        }

        model.addAttribute("doctor", new Doctor());
        model.addAttribute("doctores", doctorService.getAllDoctores());
        model.addAttribute("usuarioLogueado", u);
        return "ADMIN/gestionDoctores";
    }

    @PostMapping("/doctores/agregar")
    public String agregarDoctor(@Valid @ModelAttribute("doctor") Doctor doctor,
                              BindingResult result,
                              HttpSession session,
                              Model model,
                              @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {

        Usuario u = (Usuario) session.getAttribute("usuarioLogueado");
        if (u == null || !u.getRol().getNombre().equals("ROLE_ADMIN")) {
            return "redirect:/login";
        }

        if (result.hasErrors()) {
            model.addAttribute("doctores", doctorService.getAllDoctores());
            return "ADMIN/gestionDoctores";
        }

        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                doctorService.guardarDoctor(doctor, imageFile);
            } else {
                doctorService.guardarDoctor(doctor);
            }
            return "redirect:/ADMIN/doctores?success=created";
        } catch (RuntimeException e) {
            model.addAttribute("doctores", doctorService.getAllDoctores());
            model.addAttribute("error", e.getMessage());
            return "ADMIN/gestionDoctores";
        }
    }

    @PostMapping("/doctores/eliminar/{id}")
    public String eliminarDoctor(@PathVariable Long id, HttpSession session) {
        Usuario u = (Usuario) session.getAttribute("usuarioLogueado");
        if (u == null || !u.getRol().getNombre().equals("ROLE_ADMIN")) {
            return "redirect:/login";
        }

        try {
            doctorService.eliminarDoctor(id);
            return "redirect:/ADMIN/doctores?success=deleted";
        } catch (Exception e) {
            return "redirect:/ADMIN/doctores?error=delete_failed";
        }
    }

    // ✅ Confirmar Cita asignando un doctor
    @PostMapping("/citas/confirmar/{id}")
    public String confirmarCita(@PathVariable Long id, @RequestParam Long doctorId, HttpSession session) {
        Usuario u = (Usuario) session.getAttribute("usuarioLogueado");
        if (u == null || !u.getRol().getNombre().equals("ROLE_ADMIN")) {
            return "redirect:/login";
        }

        Cita cita = citaService.obtenerPorId(id);
        Doctor doctor = doctorService.getAllDoctores().stream()
                .filter(d -> d.getId().equals(doctorId))
                .findFirst().orElse(null);

        if (cita != null && doctor != null) {
            cita.setEstado("CONFIRMADA");
            cita.setDoctor(doctor);
            citaService.guardar(cita);
            return "redirect:/ADMIN/dashboard?success=appointment_confirmed";
        }

        return "redirect:/ADMIN/dashboard?error=appointment_confirmation_failed";
    }

    // ✅ Cancelar Cita
    @PostMapping("/citas/cancelar/{id}")
    public String cancelarCita(@PathVariable Long id, HttpSession session) {
        Usuario u = (Usuario) session.getAttribute("usuarioLogueado");
        if (u == null || !u.getRol().getNombre().equals("ROLE_ADMIN")) {
            return "redirect:/login";
        }

        Cita cita = citaService.obtenerPorId(id);
        if (cita != null) {
            cita.setEstado("CANCELADA");
            citaService.guardar(cita);
            return "redirect:/ADMIN/dashboard?success=appointment_canceled";
        }

        return "redirect:/ADMIN/dashboard?error=appointment_cancelation_failed";
    }

    // ✅ Registrar Examen Clínico para un Usuario
    @PostMapping("/usuarios/examenes/agregar")
    public String agregarExamen(@RequestParam Long usuarioId,
                                 @RequestParam String tipo,
                                 @RequestParam String descripcion,
                                 @RequestParam String fecha,
                                 HttpSession session) {
        Usuario u = (Usuario) session.getAttribute("usuarioLogueado");
        if (u == null || !u.getRol().getNombre().equals("ROLE_ADMIN")) {
            return "redirect:/login";
        }

        Usuario paciente = usuarioService.getAll().stream()
                .filter(usr -> usr.getId().equals(usuarioId))
                .findFirst().orElse(null);

        if (paciente != null) {
            Examen examen = new Examen();
            examen.setUsuario(paciente);
            examen.setTipo(tipo);
            examen.setDescripcion(descripcion);
            examen.setFecha(LocalDate.parse(fecha));
            examenService.guardar(examen);
            return "redirect:/ADMIN/dashboard?success=exam_added";
        }

        return "redirect:/ADMIN/dashboard?error=exam_add_failed";
    }
}
