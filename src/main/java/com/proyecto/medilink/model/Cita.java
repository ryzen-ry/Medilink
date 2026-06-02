package com.proyecto.medilink.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "citas")
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Datos del paciente (opcional si el usuario está logueado, pero guardamos para historial)
    @NotBlank(message = "El nombre del paciente es obligatorio")
    private String nombrePaciente;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Formato de email incorrecto")
    private String emailPaciente;

    @NotBlank(message = "El teléfono es obligatorio")
    private String telefonoPaciente;

    // Fecha de nacimiento del paciente (opcional)
    private LocalDate fechaNacimiento;

    // Especialidad elegida (p. ej. Pediatría)
    @NotBlank(message = "La especialidad es obligatoria")
    private String especialidad;

    // Fecha preferida para la cita (guardada como fecha)
    @NotNull(message = "La fecha de la cita es obligatoria")
    private LocalDate fecha;

    @NotBlank(message = "El motivo de la cita es obligatorio")
    private String motivo;

    // Relación con Usuario (si el usuario está logueado)
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    // Metadatos
    private String estado; // p.ej. PENDIENTE, CONFIRMADA, CANCELADA

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.estado == null) this.estado = "PENDIENTE";
    }
}
