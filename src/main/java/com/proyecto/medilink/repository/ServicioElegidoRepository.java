package com.proyecto.medilink.repository;

import com.proyecto.medilink.model.ServicioElegido;
import com.proyecto.medilink.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServicioElegidoRepository extends JpaRepository<ServicioElegido, Long> {
    List<ServicioElegido> findByUsuario(Usuario usuario);
}
