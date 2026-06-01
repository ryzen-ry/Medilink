package com.proyecto.medilink.controller;

import com.proyecto.medilink.model.Usuario;
import com.proyecto.medilink.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RedirectController {

    private final UsuarioService usuarioService;

    public RedirectController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/redireccion")
    public String redireccion(HttpSession session) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Usuario u = usuarioService.findByEmail(email);

        // Guardas el usuario en sesión (como lo hacías antes)
        session.setAttribute("usuarioLogueado", u);

        // Redirección por rol
        String rol = u.getRol().getNombre(); // "ROLE_ADMIN" / "ROLE_USER"

        if (rol.equals("ROLE_ADMIN")) {
            return "redirect:/ADMIN/dashboard";
        } else if (rol.equals("ROLE_USER")) {
            return "redirect:/USER/indexUser";
        }

        return "redirect:/";
    }
}
