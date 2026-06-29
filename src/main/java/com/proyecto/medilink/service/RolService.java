package com.proyecto.medilink.service;

import com.proyecto.medilink.model.Rol;
import com.proyecto.medilink.repository.RolRepository;
import org.springframework.stereotype.Service;


@Service
public class RolService {

    private final RolRepository rolRepository;

    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    public Rol findByNombre(String nombre) {
        return rolRepository.findByNombre(nombre);
    }
}
