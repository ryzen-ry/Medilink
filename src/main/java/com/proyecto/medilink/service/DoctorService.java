package com.proyecto.medilink.service;

import com.proyecto.medilink.model.Doctor;
import com.proyecto.medilink.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    public List<Doctor> getAllDoctores() {
        return doctorRepository.findAll();
    }

    public Doctor guardarDoctor(Doctor doctor) {
        // Mantener compatibilidad: sin imagen
        if (doctorRepository.findByEmail(doctor.getEmail()) != null) {
            throw new RuntimeException("El email ya está registrado");
        }
        return doctorRepository.save(doctor);
    }

    public Doctor guardarDoctor(Doctor doctor, MultipartFile imageFile) {
        if (doctorRepository.findByEmail(doctor.getEmail()) != null) {
            throw new RuntimeException("El email ya está registrado");
        }

        // Guardar la imagen si fue enviada
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                Path imagesDir = Paths.get("src/main/resources/static/img/doctors");
                if (!Files.exists(imagesDir)) {
                    Files.createDirectories(imagesDir);
                }

                String original = imageFile.getOriginalFilename();
                String filename = System.currentTimeMillis() + "_" + (original != null ? original.replaceAll("\\s+", "_") : "img.jpg");
                Path target = imagesDir.resolve(filename);
                Files.copy(imageFile.getInputStream(), target);
                doctor.setImagen(filename);
            } catch (IOException e) {
                throw new RuntimeException("Error al guardar la imagen del doctor", e);
            }
        }

        return doctorRepository.save(doctor);
    }

    public void eliminarDoctor(Long id) {
        doctorRepository.deleteById(id);
    }

    public Doctor findById(Long id) {
        return doctorRepository.findById(id).orElse(null);
    }
}