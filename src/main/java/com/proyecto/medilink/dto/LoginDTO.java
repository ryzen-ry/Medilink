package com.proyecto.medilink.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDTO {

    @Email(message = "Correo inválido")
    private String email;

    @NotBlank(message = "Ingrese su contraseña")
    private String password;
}
