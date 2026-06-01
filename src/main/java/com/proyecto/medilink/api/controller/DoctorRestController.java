package com.proyecto.medilink.api.controller;

import com.proyecto.medilink.api.response.ApiResponse;
import com.proyecto.medilink.api.response.DoctorResponse;
import com.proyecto.medilink.model.Doctor;
import com.proyecto.medilink.service.DoctorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/doctores")
@CrossOrigin(origins = "*", maxAge = 3600)
public class DoctorRestController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<DoctorResponse>>> getAllDoctores() {
        try {
            List<DoctorResponse> doctores = doctorService.getAllDoctores().stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponse.success("Doctores recuperados exitosamente", doctores));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al recuperar doctores: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DoctorResponse>> getDoctorById(@PathVariable Long id) {
        try {
            Doctor doctor = doctorService.findById(id);
            if (doctor == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Doctor no encontrado"));
            }
            return ResponseEntity.ok(ApiResponse.success(convertToResponse(doctor)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DoctorResponse>> createDoctor(@Valid @RequestBody Doctor doctor) {
        try {
            Doctor savedDoctor = doctorService.guardarDoctor(doctor);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Doctor creado exitosamente", convertToResponse(savedDoctor)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DoctorResponse>> updateDoctor(@PathVariable Long id,
                                                                     @Valid @RequestBody Doctor doctorDetails) {
        try {
            Doctor doctor = doctorService.findById(id);
            if (doctor == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Doctor no encontrado"));
            }

            if (doctorDetails.getNombre() != null) doctor.setNombre(doctorDetails.getNombre());
            if (doctorDetails.getApellidos() != null) doctor.setApellidos(doctorDetails.getApellidos());
            if (doctorDetails.getEspecialidad() != null) doctor.setEspecialidad(doctorDetails.getEspecialidad());
            if (doctorDetails.getNumeroColegiatura() != null) doctor.setNumeroColegiatura(doctorDetails.getNumeroColegiatura());
            if (doctorDetails.getTelefono() != null) doctor.setTelefono(doctorDetails.getTelefono());

            Doctor updatedDoctor = doctorService.guardarDoctor(doctor);
            return ResponseEntity.ok(ApiResponse.success("Doctor actualizado exitosamente", convertToResponse(updatedDoctor)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Error al actualizar el doctor: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDoctor(@PathVariable Long id) {
        try {
            Doctor doctor = doctorService.findById(id);
            if (doctor == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Doctor no encontrado"));
            }
            doctorService.eliminarDoctor(id);
            return ResponseEntity.ok(ApiResponse.success("Doctor eliminado exitosamente", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Error al eliminar el doctor: " + e.getMessage()));
        }
    }

    private DoctorResponse convertToResponse(Doctor doctor) {
        return new DoctorResponse(
                doctor.getId(),
                doctor.getNombre(),
                doctor.getApellidos(),
                doctor.getEspecialidad(),
                doctor.getNumeroColegiatura(),
                doctor.getEmail(),
                doctor.getTelefono(),
                doctor.getImagen()
        );
    }
}

