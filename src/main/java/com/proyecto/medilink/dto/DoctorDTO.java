package com.proyecto.medilink.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class DoctorDTO {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "Los apellidos son obligatorios")
    private String apellidos;

    @NotBlank(message = "La especialidad es obligatoria")
    private String especialidad;

    @NotBlank(message = "El número de colegiatura es obligatorio")
    private String numeroColegiatura;

    @Email(message = "Formato de email incorrecto")
    @NotBlank(message = "El email es obligatorio")
    private String email;

    @Pattern(regexp = "^\\d{9}$", message = "El teléfono debe tener 9 dígitos")
    private String telefono;

    private String imagen;
}