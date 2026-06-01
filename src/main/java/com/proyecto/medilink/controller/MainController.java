package com.proyecto.medilink.controller;

import com.proyecto.medilink.model.Usuario;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

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
        return "redirect:/login";
    }

    @GetMapping("/citas")
    public String citas() {
        return "redirect:/login";
    }

    @GetMapping("/contacto")
    public String contacto() {
        return "redirect:/login";
    }

    @GetMapping("/registro")
    public String registro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }
}
