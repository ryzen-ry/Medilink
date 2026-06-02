package com.proyecto.medilink.service;

import com.proyecto.medilink.model.ServicioElegido;
import com.proyecto.medilink.model.Usuario;
import com.proyecto.medilink.repository.ServicioElegidoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioElegidoService {

    private final ServicioElegidoRepository repo;

    public ServicioElegidoService(ServicioElegidoRepository repo) {
        this.repo = repo;
    }

    public ServicioElegido guardar(ServicioElegido se) {
        return repo.save(se);
    }

    public List<ServicioElegido> listarPorUsuario(Usuario usuario) {
        return repo.findByUsuario(usuario);
    }
}
