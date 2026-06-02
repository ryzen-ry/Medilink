package com.proyecto.medilink.api.controller;

import com.proyecto.medilink.api.response.ApiResponse;
import com.proyecto.medilink.api.response.CitaResponse;
import com.proyecto.medilink.model.Cita;
import com.proyecto.medilink.model.Usuario;
import com.proyecto.medilink.service.CitaService;
import com.proyecto.medilink.service.UsuarioService;
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
@RequestMapping("/api/v1/citas")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Citas", description = "Endpoints para gestión de citas médicas")
public class CitaRestController {

    @Autowired
    private CitaService citaService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    @Operation(summary = "Obtener todas las citas", description = "Recupera la lista de todas las citas médicas")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Citas recuperadas exitosamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<List<CitaResponse>>> obtenerTodasLasCitas() {
        try {
            List<CitaResponse> citas = List.of();
            return ResponseEntity.ok(ApiResponse.success("Citas recuperadas", citas));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al recuperar citas: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener cita por ID", description = "Recupera los detalles de una cita específica")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Cita encontrada"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Cita no encontrada"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<CitaResponse>> obtenerCitaPorId(
            @io.swagger.v3.oas.annotations.Parameter(description = "ID de la cita")
            @PathVariable Long id) {
        try {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Cita no encontrada"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error: " + e.getMessage()));
        }
    }

    @PostMapping
    @Operation(summary = "Crear nueva cita", description = "Registra una nueva cita médica en el sistema")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Cita creada exitosamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    public ResponseEntity<ApiResponse<CitaResponse>> crearCita(
            @RequestBody(description = "Datos de la cita a crear")
            @Valid @org.springframework.web.bind.annotation.RequestBody Cita cita) {
        try {
            Cita savedCita = citaService.guardar(cita);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Cita creada exitosamente", convertToResponse(savedCita)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Error al crear la cita: " + e.getMessage()));
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Obtener citas por usuario", description = "Recupera todas las citas asociadas a un usuario específico")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Citas del usuario recuperadas"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<List<CitaResponse>>> obtenerCitasPorUsuario(
            @io.swagger.v3.oas.annotations.Parameter(description = "ID del usuario")
            @PathVariable Long usuarioId) {
        try {
            Usuario usuario = usuarioService.findById(usuarioId);
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Usuario no encontrado"));
            }
            List<CitaResponse> citas = citaService.listarPorUsuario(usuario).stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponse.success("Citas del usuario recuperadas", citas));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al recuperar citas: " + e.getMessage()));
        }
    }

    private CitaResponse convertToResponse(Cita cita) {
        CitaResponse response = new CitaResponse();
        response.setId(cita.getId());
        response.setNombrePaciente(cita.getNombrePaciente());
        response.setEmailPaciente(cita.getEmailPaciente());
        response.setTelefonoPaciente(cita.getTelefonoPaciente());
        response.setFechaNacimiento(cita.getFechaNacimiento());
        response.setEspecialidad(cita.getEspecialidad());
        response.setFecha(cita.getFecha());
        response.setMotivo(cita.getMotivo());
        response.setEstado(cita.getEstado());
        response.setCreatedAt(cita.getCreatedAt());
        if (cita.getUsuario() != null) {
            response.setUsuarioId(cita.getUsuario().getId());
        }
        return response;
    }
}

