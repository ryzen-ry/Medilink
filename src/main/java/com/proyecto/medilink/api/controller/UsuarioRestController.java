package com.proyecto.medilink.api.controller;

import com.proyecto.medilink.api.response.ApiResponse;
import com.proyecto.medilink.api.response.UsuarioResponse;
import com.proyecto.medilink.model.Usuario;
import com.proyecto.medilink.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/usuarios")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Usuarios", description = "Endpoints para gestión de usuarios")
public class UsuarioRestController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    @Operation(summary = "Obtener todos los usuarios", description = "Recupera la lista completa de usuarios registrados")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Usuarios recuperados exitosamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<List<UsuarioResponse>>> obtenerTodosLosUsuarios() {
        try {
            List<UsuarioResponse> usuarios = usuarioService.getAll().stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponse.success("Usuarios recuperados exitosamente", usuarios));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al recuperar usuarios: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por ID", description = "Recupera los detalles de un usuario específico")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Usuario encontrado"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponse<UsuarioResponse>> obtenerUsuario(
            @io.swagger.v3.oas.annotations.Parameter(description = "ID del usuario")
            @PathVariable Long id) {
        try {
            Usuario usuario = usuarioService.findById(id);
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Usuario no encontrado"));
            }
            return ResponseEntity.ok(ApiResponse.success(convertToResponse(usuario)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error: " + e.getMessage()));
        }
    }

    @PostMapping("/registro")
    @Operation(summary = "Registrar nuevo usuario", description = "Crea un nuevo usuario en el sistema")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "El correo ya existe"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    public ResponseEntity<ApiResponse<UsuarioResponse>> registrarUsuario(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del usuario a registrar")
            @Valid @org.springframework.web.bind.annotation.RequestBody Usuario usuario) {
        try {
            if (usuarioService.findByEmail(usuario.getEmail()) != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.error("El correo ya existe"));
            }
            Usuario usuarioRegistrado = usuarioService.registrarUsuario(usuario);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Usuario registrado exitosamente", convertToResponse(usuarioRegistrado)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Error al registrar usuario: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario", description = "Actualiza los datos de un usuario existente")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    public ResponseEntity<ApiResponse<UsuarioResponse>> actualizarUsuario(
            @io.swagger.v3.oas.annotations.Parameter(description = "ID del usuario")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos actualizados del usuario")
            @Valid @org.springframework.web.bind.annotation.RequestBody Usuario usuarioDetails) {
        try {
            Usuario usuario = usuarioService.findById(id);
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Usuario no encontrado"));
            }

            if (usuarioDetails.getNombre() != null) usuario.setNombre(usuarioDetails.getNombre());
            if (usuarioDetails.getDni() != null) usuario.setDni(usuarioDetails.getDni());
            if (usuarioDetails.getTelefono() != null) usuario.setTelefono(usuarioDetails.getTelefono());

            Usuario usuarioActualizado = usuarioService.saveUsuario(usuario);
            return ResponseEntity.ok(ApiResponse.success("Usuario actualizado exitosamente", convertToResponse(usuarioActualizado)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Error al actualizar usuario: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario del sistema")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Usuario eliminado exitosamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Error al eliminar usuario")
    })
    public ResponseEntity<ApiResponse<Void>> eliminarUsuario(
            @io.swagger.v3.oas.annotations.Parameter(description = "ID del usuario")
            @PathVariable Long id) {
        try {
            Usuario usuario = usuarioService.findById(id);
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Usuario no encontrado"));
            }
            usuarioService.eliminarUsuario(id, null);
            return ResponseEntity.ok(ApiResponse.success("Usuario eliminado exitosamente", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Error al eliminar usuario: " + e.getMessage()));
        }
    }

    private UsuarioResponse convertToResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getDni(),
                usuario.getTelefono(),
                usuario.getEmail(),
                usuario.getRol() != null ? usuario.getRol().getNombre() : "N/A"
        );
    }
}
