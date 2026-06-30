package com.proyecto.medilink.model;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;

@Entity
@Data
@Table(name = "doctores")
public class Doctor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String apellidos;

    private String especialidad;

    private String numeroColegiatura;

    @Column(unique = true, nullable = false)
    private String email;

    private String telefono;

    // Nombre de archivo de la imagen del doctor almacenada en /static/img/doctors/
    private String imagen;
}