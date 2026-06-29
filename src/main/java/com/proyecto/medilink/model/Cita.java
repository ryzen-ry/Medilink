package com.proyecto.medilink.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
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

    // Relación con Doctor (asignado al confirmar la cita)
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    // Metadatos
    private String estado; // p.ej. PENDIENTE, CONFIRMADA, CANCELADA

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now(ZoneId.of("America/Lima"));
        if (this.estado == null) this.estado = "PENDIENTE";
    }

    // Constructor vacío
    public Cita() {}

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombrePaciente() {
        return nombrePaciente;
    }

    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }

    public String getEmailPaciente() {
        return emailPaciente;
    }

    public void setEmailPaciente(String emailPaciente) {
        this.emailPaciente = emailPaciente;
    }

    public String getTelefonoPaciente() {
        return telefonoPaciente;
    }

    public void setTelefonoPaciente(String telefonoPaciente) {
        this.telefonoPaciente = telefonoPaciente;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}