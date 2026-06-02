package com.proyecto.medilink.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "DTO para autenticación de usuarios")
public class LoginDTO {

    @Email(message = "Correo inválido")
    @Schema(description = "Correo electrónico del usuario", example = "usuario@ejemplo.com")
    private String email;

    @NotBlank(message = "Ingrese su contraseña")
    @Schema(description = "Contraseña del usuario", example = "miContraseña123")
    private String password;
}
