package com.proyecto.medilink.api.controller;

import com.proyecto.medilink.api.response.ApiResponse;
import com.proyecto.medilink.dto.LoginDTO;
import com.proyecto.medilink.model.Usuario;
import com.proyecto.medilink.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Autenticación", description = "Endpoints de autenticación de usuarios")
public class AuthRestController {

    @Autowired
    private UsuarioService usuarioService;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Autentica un usuario con correo y contraseña")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Login exitoso"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Credenciales incorrectas"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    public ResponseEntity<ApiResponse<Map<String, Object>>> login(@Valid @RequestBody LoginDTO loginDTO,
                                                                   HttpSession session) {
        try {
            Usuario usuario = usuarioService.findByEmail(loginDTO.getEmail());
            
            if (usuario == null || !passwordEncoder.matches(loginDTO.getPassword(), usuario.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("Correo o contraseña incorrectos"));
            }

            // Guardar en sesión
            session.setAttribute("usuarioLogueado", usuario);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("id", usuario.getId());
            responseData.put("email", usuario.getEmail());
            responseData.put("nombre", usuario.getNombre());
            responseData.put("rol", usuario.getRol().getNombre());
            responseData.put("sessionId", session.getId());

            return ResponseEntity.ok(ApiResponse.success("Login exitoso", responseData));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Error durante el login: " + e.getMessage()));
        }
    }

    @PostMapping("/logout")
    @Operation(summary = "Cerrar sesión", description = "Invalida la sesión del usuario")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Logout exitoso"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Error al hacer logout")
    })
    public ResponseEntity<ApiResponse<Void>> logout(HttpSession session) {
        try {
            session.invalidate();
            return ResponseEntity.ok(ApiResponse.success("Logout exitoso", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Error durante el logout: " + e.getMessage()));
        }
    }

    @GetMapping("/me")
    @Operation(summary = "Obtener usuario autenticado", description = "Retorna información del usuario autenticado en la sesión actual")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Usuario recuperado exitosamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No hay usuario autenticado"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Error al recuperar usuario")
    })
    public ResponseEntity<ApiResponse<Map<String, Object>>> getCurrentUser(HttpSession session) {
        try {
            Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
            
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("No hay usuario autenticado"));
            }

            Map<String, Object> userData = new HashMap<>();
            userData.put("id", usuario.getId());
            userData.put("email", usuario.getEmail());
            userData.put("nombre", usuario.getNombre());
            userData.put("rol", usuario.getRol().getNombre());

            return ResponseEntity.ok(ApiResponse.success(userData));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Error: " + e.getMessage()));
        }
    }
}
