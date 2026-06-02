package com.proyecto.medilink.service;

import com.proyecto.medilink.model.Examen;
import com.proyecto.medilink.model.Usuario;
import com.proyecto.medilink.repository.ExamenRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamenService {

    private final ExamenRepository repo;

    public ExamenService(ExamenRepository repo) {
        this.repo = repo;
    }

    public Examen guardar(Examen examen) {
        return repo.save(examen);
    }

    public List<Examen> examsPorUsuario(Usuario usuario) {
        return repo.findByUsuario(usuario);
    }
}
