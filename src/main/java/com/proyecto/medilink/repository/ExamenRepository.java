package com.proyecto.medilink.repository;

import com.proyecto.medilink.model.Examen;
import com.proyecto.medilink.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamenRepository extends JpaRepository<Examen, Long> {
    List<Examen> findByUsuario(Usuario usuario);
}
