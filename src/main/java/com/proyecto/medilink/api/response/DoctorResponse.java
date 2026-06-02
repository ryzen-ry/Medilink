package com.proyecto.medilink.api.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de respuesta para información de doctores")
public class DoctorResponse {
    @Schema(description = "ID único del doctor", example = "1")
    private Long id;
    
    @Schema(description = "Nombre del doctor", example = "Carlos")
    private String nombre;
    
    @Schema(description = "Apellidos del doctor", example = "García López")
    private String apellidos;
    
    @Schema(description = "Especialidad médica", example = "Cardiología")
    private String especialidad;
    
    @Schema(description = "Número de colegiatura", example = "COL123456")
    private String numeroColegiatura;
    
    @Schema(description = "Correo electrónico del doctor", example = "carlos@ejemplo.com")
    private String email;
    
    @Schema(description = "Teléfono del doctor", example = "555-5678")
    private String telefono;
    
    @Schema(description = "URL de la imagen del doctor")
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
