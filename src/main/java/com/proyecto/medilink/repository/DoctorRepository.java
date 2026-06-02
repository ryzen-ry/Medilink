package com.proyecto.medilink.repository;

import com.proyecto.medilink.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Doctor findByEmail(String email);
}