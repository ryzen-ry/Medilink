package com.proyecto.medilink.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CitaResponse {
    private Long id;
    private String nombrePaciente;
    private String emailPaciente;
    private String telefonoPaciente;
    private LocalDate fechaNacimiento;
    private String especialidad;
    private LocalDate fecha;
    private String motivo;
    private String estado;
    private LocalDateTime createdAt;
    private Long usuarioId;
}
