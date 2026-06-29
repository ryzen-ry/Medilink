package com.proyecto.medilink.controller;

import com.proyecto.medilink.dto.LoginDTO;
import com.proyecto.medilink.dto.UsuarioDTO;
import com.proyecto.medilink.model.Usuario;
import com.proyecto.medilink.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UsuarioService usuarioService;

    // ✅ Constructor injection (en lugar de @Autowired en campos)
    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("loginDTO", new LoginDTO());
        return "login";
    }

    
    @PostMapping("/registro")
    public String procesarRegistro(@Valid @ModelAttribute("usuario") UsuarioDTO usuarioDTO,
                                   BindingResult result) {

        if (result.hasErrors()) return "registro";

        if (usuarioService.findByEmail(usuarioDTO.getEmail()) != null) {
            result.rejectValue("email", "error.usuario", "El correo ya existe");
            return "registro";
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setDni(usuarioDTO.getDni());
        usuario.setTelefono(usuarioDTO.getTelefono());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setPassword(usuarioDTO.getPassword());

        usuarioService.registrarUsuario(usuario);
        return "redirect:/login?success";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}