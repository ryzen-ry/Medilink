package com.proyecto.medilink.controller;

import com.proyecto.medilink.dto.UsuarioDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    private static final String REDIRECT_LOGIN = "redirect:/login";

    @GetMapping("/principal")
    public String home() {
        return "index";
    }
    @GetMapping("/nosotros")
    public String nosotros() {
        return "nosotros";
    }

    @GetMapping("/servicios")
    public String servicios() {
        return REDIRECT_LOGIN;
    }

    @GetMapping("/citas")
    public String citas() {
        return REDIRECT_LOGIN;
    }

    @GetMapping("/contacto")
    public String contacto() {
        return REDIRECT_LOGIN;
    }

    @GetMapping("/registro")
    public String registro(Model model) {
        model.addAttribute("usuario", new UsuarioDTO());
        return "registro";
    }
}