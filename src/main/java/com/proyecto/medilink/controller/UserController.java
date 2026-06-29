package com.proyecto.medilink.controller;

import com.proyecto.medilink.service.DoctorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/USER")
public class UserController {

    private final DoctorService doctorService;

    // ✅ Constructor injection (en lugar de @Autowired en campos)
    public UserController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping("/indexUser")
    public String indexUser() {
        return "USER/indexUser";
    }
    @GetMapping("/nosotrosUser")
    public String nosotrosUser(){
        return "USER/nosotrosUser";
    }
    
    @GetMapping("/citasUser")
    public String citasUser(){
        return "USER/citasUser";
    }
    @GetMapping("/contactoUser")
    public String contactoUser(Model model){
        model.addAttribute("doctores", doctorService.getAllDoctores());
        return "USER/contactoUser";
    }
    
    
}