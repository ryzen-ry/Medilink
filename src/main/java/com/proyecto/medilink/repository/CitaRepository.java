package com.proyecto.medilink.repository;

import com.proyecto.medilink.model.Cita;
import com.proyecto.medilink.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Long> {
    List<Cita> findByUsuario(Usuario usuario);
}
