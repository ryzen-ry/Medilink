package com.proyecto.medilink.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UsuarioDTO {

    @NotBlank(message = "Ingrese su nombre completo")
    private String nombre;

    @Size(min = 8, max = 8, message = "El DNI debe tener 8 dígitos")
    private String dni;

    @Size(min = 9, max = 9, message = "El teléfono debe tener 9 dígitos")
    private String telefono;

    @NotNull(message = "Correo requerido")
    @Email(message = "Formato de email incorrecto")
    private String email;

    @NotBlank(message = "Ingrese contraseña")
    private String password;
}
