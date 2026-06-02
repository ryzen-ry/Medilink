package com.proyecto.medilink.api.controller;

import com.proyecto.medilink.api.response.ApiResponse;
import com.proyecto.medilink.api.response.DoctorResponse;
import com.proyecto.medilink.model.Doctor;
import com.proyecto.medilink.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
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
@Tag(name = "Doctores", description = "Endpoints para gestión de doctores")
public class DoctorRestController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping
    @Operation(summary = "Obtener todos los doctores", description = "Recupera la lista completa de doctores registrados")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Doctores recuperados exitosamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
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
    @Operation(summary = "Obtener doctor por ID", description = "Recupera los detalles de un doctor específico")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Doctor encontrado"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Doctor no encontrado"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<DoctorResponse>> getDoctorById(
            @io.swagger.v3.oas.annotations.Parameter(description = "ID del doctor")
            @PathVariable Long id) {
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
    @Operation(summary = "Crear nuevo doctor", description = "Registra un nuevo doctor en el sistema")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Doctor creado exitosamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    public ResponseEntity<ApiResponse<DoctorResponse>> createDoctor(
            @RequestBody(description = "Datos del doctor a crear")
            @Valid @org.springframework.web.bind.annotation.RequestBody Doctor doctor) {
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
    @Operation(summary = "Actualizar doctor", description = "Actualiza los datos de un doctor existente")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Doctor actualizado exitosamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Doctor no encontrado"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    public ResponseEntity<ApiResponse<DoctorResponse>> updateDoctor(
            @io.swagger.v3.oas.annotations.Parameter(description = "ID del doctor")
            @PathVariable Long id,
            @RequestBody(description = "Datos actualizados del doctor")
            @Valid @org.springframework.web.bind.annotation.RequestBody Doctor doctorDetails) {
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
    @Operation(summary = "Eliminar doctor", description = "Elimina un doctor del sistema")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Doctor eliminado exitosamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Doctor no encontrado"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Error al eliminar doctor")
    })
    public ResponseEntity<ApiResponse<Void>> deleteDoctor(
            @io.swagger.v3.oas.annotations.Parameter(description = "ID del doctor")
            @PathVariable Long id) {
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

