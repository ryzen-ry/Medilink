package com.proyecto.medilink.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Data
@Table(name = "doctores")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @Column(unique = true)
    private String email;

    @Pattern(regexp = "^[0-9]{9}$", message = "El teléfono debe tener 9 dígitos")
    private String telefono;

    // Nombre de archivo de la imagen del doctor almacenada en /static/img/doctors/
    private String imagen;
}