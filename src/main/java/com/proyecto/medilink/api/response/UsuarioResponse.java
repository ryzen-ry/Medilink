package com.proyecto.medilink.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponse {
    private Long id;
    private String nombre;
    private String dni;
    private String telefono;
    private String email;
    private String rolNombre;

    public UsuarioResponse(Long id, String nombre, String dni, String email, String rolNombre) {
        this.id = id;
        this.nombre = nombre;
        this.dni = dni;
        this.email = email;
        this.rolNombre = rolNombre;
    }
}
