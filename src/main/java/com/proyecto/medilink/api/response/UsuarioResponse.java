package com.proyecto.medilink.api.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de respuesta para información de usuarios")
public class UsuarioResponse {
    @Schema(description = "ID único del usuario", example = "1")
    private Long id;
    
    @Schema(description = "Nombre completo del usuario", example = "Juan Pérez")
    private String nombre;
    
    @Schema(description = "Número de DNI del usuario", example = "12345678")
    private String dni;
    
    @Schema(description = "Número de teléfono del usuario", example = "555-1234")
    private String telefono;
    
    @Schema(description = "Correo electrónico del usuario", example = "juan@ejemplo.com")
    private String email;
    
    @Schema(description = "Nombre del rol del usuario", example = "USER")
    private String rolNombre;

    public UsuarioResponse(Long id, String nombre, String dni, String email, String rolNombre) {
        this.id = id;
        this.nombre = nombre;
        this.dni = dni;
        this.email = email;
        this.rolNombre = rolNombre;
    }
}
