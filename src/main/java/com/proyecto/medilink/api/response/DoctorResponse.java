package com.proyecto.medilink.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorResponse {
    private Long id;
    private String nombre;
    private String apellidos;
    private String especialidad;
    private String numeroColegiatura;
    private String email;
    private String telefono;
    private String imagen;

    public DoctorResponse(Long id, String nombre, String apellidos, String especialidad, 
                         String email, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.especialidad = especialidad;
        this.email = email;
        this.telefono = telefono;
    }
}
