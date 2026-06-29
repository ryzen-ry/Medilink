package com.proyecto.medilink.controller;

import com.proyecto.medilink.dto.DoctorDTO;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@Controller
@RequestMapping("/ADMIN")
public class AdminController {

    private static final String USUARIO_LOGUEADO = "usuarioLogueado";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String REDIRECT_LOGIN = "redirect:/login";
    private static final String DOCTORES = "doctores";
    private static final String GESTION_DOCTORES = "ADMIN/gestionDoctores";

    private final UsuarioService usuarioService;
    private final DoctorService doctorService;
    private final CitaService citaService;
    private final ExamenService examenService;

    public AdminController(UsuarioService usuarioService,
                           DoctorService doctorService,
                           CitaService citaService,
                           ExamenService examenService) {
        this.usuarioService = usuarioService;
        this.doctorService = doctorService;
        this.citaService = citaService;
        this.examenService = examenService;
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Usuario u = (Usuario) session.getAttribute(USUARIO_LOGUEADO);

        if (u == null || !u.getRol().getNombre().equals(ROLE_ADMIN)) {
            return REDIRECT_LOGIN;
        }

        model.addAttribute(USUARIO_LOGUEADO, u);
        model.addAttribute("usuarios", usuarioService.getAll());
        model.addAttribute(DOCTORES, doctorService.getAllDoctores());
        model.addAttribute("citas", citaService.listarTodas());

        return "ADMIN/dashboard";
    }


    @GetMapping("/usuarios")
    public String listarUsuarios(HttpSession session, Model model) {
        Usuario u = (Usuario) session.getAttribute(USUARIO_LOGUEADO);

        if (u == null || !u.getRol().getNombre().equals(ROLE_ADMIN)) {
            return REDIRECT_LOGIN;
        }

        return "redirect:/ADMIN/dashboard";
    }

    @PostMapping("/cambiar-rol/{id}")
    public String cambiarRol(@PathVariable Long id,
                             @RequestParam String nuevoRol,
                             HttpSession session) {

        Usuario u = (Usuario) session.getAttribute(USUARIO_LOGUEADO);

        if (u == null || !u.getRol().getNombre().equals(ROLE_ADMIN)) {
            return REDIRECT_LOGIN;
        }

        usuarioService.cambiarRol(id, nuevoRol);
        return "redirect:/ADMIN/usuarios";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id,
                                  HttpSession session) {

        Usuario adminActual = (Usuario) session.getAttribute(USUARIO_LOGUEADO);

        if (adminActual == null || !adminActual.getRol().getNombre().equals(ROLE_ADMIN)) {
            return REDIRECT_LOGIN;
        }

        try {
            boolean deleted = usuarioService.eliminarUsuario(id, adminActual.getEmail());
            if (!deleted) {
                return "redirect:/ADMIN/dashboard?error=delete_failed";
            }
            return "redirect:/ADMIN/dashboard?success=deleted";
        } catch (Exception e) {
            return "redirect:/ADMIN/usuarios?error=exception";
        }
    }

    @GetMapping("/doctores")
    public String gestionDoctores(HttpSession session, Model model) {
        Usuario u = (Usuario) session.getAttribute(USUARIO_LOGUEADO);
        if (u == null || !u.getRol().getNombre().equals(ROLE_ADMIN)) {
            return REDIRECT_LOGIN;
        }

        model.addAttribute("doctor", new DoctorDTO());
        model.addAttribute(DOCTORES, doctorService.getAllDoctores());
        model.addAttribute(USUARIO_LOGUEADO, u);
        return GESTION_DOCTORES;
    }

    @PostMapping("/doctores/agregar")
    public String agregarDoctor(@Valid @ModelAttribute("doctor") DoctorDTO doctorDTO,
                              BindingResult result,
                              HttpSession session,
                              Model model,
                              @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {

        Usuario u = (Usuario) session.getAttribute(USUARIO_LOGUEADO);
        if (u == null || !u.getRol().getNombre().equals(ROLE_ADMIN)) {
            return REDIRECT_LOGIN;
        }

        if (result.hasErrors()) {
            model.addAttribute(DOCTORES, doctorService.getAllDoctores());
            return GESTION_DOCTORES;
        }

        try {
            Doctor doctor = new Doctor();
            doctor.setId(doctorDTO.getId());
            doctor.setNombre(doctorDTO.getNombre());
            doctor.setApellidos(doctorDTO.getApellidos());
            doctor.setEspecialidad(doctorDTO.getEspecialidad());
            doctor.setNumeroColegiatura(doctorDTO.getNumeroColegiatura());
            doctor.setEmail(doctorDTO.getEmail());
            doctor.setTelefono(doctorDTO.getTelefono());
            doctor.setImagen(doctorDTO.getImagen());

            if (imageFile != null && !imageFile.isEmpty()) {
                doctorService.guardarDoctor(doctor, imageFile);
            } else {
                doctorService.guardarDoctor(doctor);
            }
            return "redirect:/ADMIN/doctores?success=created";
        } catch (RuntimeException e) {
            model.addAttribute(DOCTORES, doctorService.getAllDoctores());
            model.addAttribute("error", e.getMessage());
            return GESTION_DOCTORES;
        }
    }

    @PostMapping("/doctores/eliminar/{id}")
    public String eliminarDoctor(@PathVariable Long id, HttpSession session) {
        Usuario u = (Usuario) session.getAttribute(USUARIO_LOGUEADO);
        if (u == null || !u.getRol().getNombre().equals(ROLE_ADMIN)) {
            return REDIRECT_LOGIN;
        }

        try {
            doctorService.eliminarDoctor(id);
            return "redirect:/ADMIN/doctores?success=deleted";
        } catch (Exception e) {
            return "redirect:/ADMIN/doctores?error=delete_failed";
        }
    }

    @PostMapping("/citas/confirmar/{id}")
    public String confirmarCita(@PathVariable Long id, @RequestParam Long doctorId, HttpSession session) {
        Usuario u = (Usuario) session.getAttribute(USUARIO_LOGUEADO);
        if (u == null || !u.getRol().getNombre().equals(ROLE_ADMIN)) {
            return REDIRECT_LOGIN;
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

    @PostMapping("/citas/cancelar/{id}")
    public String cancelarCita(@PathVariable Long id, HttpSession session) {
        Usuario u = (Usuario) session.getAttribute(USUARIO_LOGUEADO);
        if (u == null || !u.getRol().getNombre().equals(ROLE_ADMIN)) {
            return REDIRECT_LOGIN;
        }

        Cita cita = citaService.obtenerPorId(id);
        if (cita != null) {
            cita.setEstado("CANCELADA");
            citaService.guardar(cita);
            return "redirect:/ADMIN/dashboard?success=appointment_canceled";
        }

        return "redirect:/ADMIN/dashboard?error=appointment_cancelation_failed";
    }

    @PostMapping("/usuarios/examenes/agregar")
    public String agregarExamen(@RequestParam Long usuarioId,
                                 @RequestParam String tipo,
                                 @RequestParam String descripcion,
                                 @RequestParam String fecha,
                                 HttpSession session) {
        Usuario u = (Usuario) session.getAttribute(USUARIO_LOGUEADO);
        if (u == null || !u.getRol().getNombre().equals(ROLE_ADMIN)) {
            return REDIRECT_LOGIN;
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