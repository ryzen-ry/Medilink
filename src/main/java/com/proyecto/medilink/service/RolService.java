package com.proyecto.medilink.service;

import com.proyecto.medilink.model.Rol;
import com.proyecto.medilink.repository.RolRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class RolService {

    @Autowired
    private RolRepository rolRepository;

    public Rol findByNombre(String nombre) {
        return rolRepository.findByNombre(nombre);
    }
}
