package com.proyecto.medilink.api.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de respuesta para citas médicas")
public class CitaResponse {
    @Schema(description = "ID único de la cita", example = "1")
    private Long id;
    
    @Schema(description = "Nombre del paciente", example = "Juan Pérez")
    private String nombrePaciente;
    
    @Schema(description = "Correo electrónico del paciente", example = "juan@ejemplo.com")
    private String emailPaciente;
    
    @Schema(description = "Teléfono del paciente", example = "555-1234")
    private String telefonoPaciente;
    
    @Schema(description = "Fecha de nacimiento del paciente")
    private LocalDate fechaNacimiento;
    
    @Schema(description = "Especialidad médica", example = "Cardiología")
    private String especialidad;
    
    @Schema(description = "Fecha de la cita")
    private LocalDate fecha;
    
    @Schema(description = "Motivo de la cita", example = "Revisión anual")
    private String motivo;
    
    @Schema(description = "Estado de la cita", example = "Pendiente")
    private String estado;
    
    @Schema(description = "Fecha y hora de creación de la cita")
    private LocalDateTime createdAt;
    
    @Schema(description = "ID del usuario propietario de la cita", example = "1")
    private Long usuarioId;
}
