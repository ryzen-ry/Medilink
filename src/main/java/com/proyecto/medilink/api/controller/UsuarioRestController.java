package com.proyecto.medilink.api.controller;

import com.proyecto.medilink.api.response.ApiResponse;
import com.proyecto.medilink.api.response.UsuarioResponse;
import com.proyecto.medilink.model.Usuario;
import com.proyecto.medilink.service.UsuarioService;
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
public class UsuarioRestController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
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
    public ResponseEntity<ApiResponse<UsuarioResponse>> obtenerUsuario(@PathVariable Long id) {
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
    public ResponseEntity<ApiResponse<UsuarioResponse>> registrarUsuario(@Valid @RequestBody Usuario usuario) {
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
    public ResponseEntity<ApiResponse<UsuarioResponse>> actualizarUsuario(@PathVariable Long id,
                                                                           @Valid @RequestBody Usuario usuarioDetails) {
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
    public ResponseEntity<ApiResponse<Void>> eliminarUsuario(@PathVariable Long id) {
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

