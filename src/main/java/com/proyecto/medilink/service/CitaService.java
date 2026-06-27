package com.proyecto.medilink.service;

import com.proyecto.medilink.model.Cita;
import com.proyecto.medilink.model.Usuario;
import com.proyecto.medilink.repository.CitaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CitaService {

    private final CitaRepository repo;

    public CitaService(CitaRepository repo) {
        this.repo = repo;
    }

    public Cita guardar(Cita cita) {
        return repo.save(cita);
    }

    public List<Cita> listarPorUsuario(Usuario usuario) {
        return repo.findByUsuario(usuario);
    }

    public List<Cita> listarTodas() {
        return repo.findAll();
    }

    public Cita obtenerPorId(Long id) {
        return repo.findById(id).orElse(null);
    }

    public void eliminarCita(Long id) {
        repo.deleteById(id);
    }
}
