# ❓ Preguntas Frecuentes - Swagger/OpenAPI en Medilink

## General

### ¿Qué es Swagger/OpenAPI?

**Swagger** (también llamado OpenAPI 3.0) es una especificación estándar para documentar APIs REST. Proporciona:
- Una forma estandarizada de describir tu API
- Una interfaz visual (Swagger UI) para explorar endpoints
- Capacidad de generar clientes automáticamente
- Documentación siempre actualizada

### ¿Qué es SpringDoc OpenAPI?

Es una librería que integra Swagger/OpenAPI con Spring Boot. Genera automáticamente la documentación de tu API basándose en:
- Las anotaciones de tus controladores
- Los tipos de datos de tus parámetros
- Las respuestas HTTP que devuelves

### ¿Por qué es útil?

✅ **Documentación automática**: No necesitas mantener documentación manual
✅ **Experiencia de desarrollador mejorada**: Interfaz visual fácil de usar
✅ **Testing integrado**: Prueba endpoints sin herramientas externas
✅ **Estándar de la industria**: OpenAPI es reconocido internacionalmente
✅ **Generación de clientes**: Crea SDKs automáticamente

---

## Acceso

### ¿Dónde accedo a Swagger UI?

Una vez que tu aplicación esté ejecutándose:

```
http://localhost:8080/swagger-ui.html
```

### ¿Qué puerto usa la aplicación?

Por defecto, Spring Boot usa el puerto **8080**. Si cambias el puerto en `application.properties`:

```properties
server.port=9090
```

Entonces accederías a:
```
http://localhost:9090/swagger-ui.html
```

### ¿Cómo veo la especificación OpenAPI en formato JSON?

En formato JSON:
```
http://localhost:8080/v3/api-docs
```

En formato YAML:
```
http://localhost:8080/v3/api-docs.yaml
```

### ¿Puedo acceder a Swagger desde fuera del localhost?

Sí, siempre que la aplicación esté accesible. Si despliegas en un servidor:

```
http://tu-servidor.com:8080/swagger-ui.html
```

---

## Documentación

### ¿Cómo agrego documentación a mis endpoints?

Usa anotaciones OpenAPI:

```java
@PostMapping("/crear")
@Operation(summary = "Crear nuevo usuario", 
           description = "Crea un nuevo usuario en el sistema")
@ApiResponses(value = {
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", 
        description = "Usuario creado exitosamente"),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", 
        description = "Datos inválidos")
})
public ResponseEntity<ApiResponse<UsuarioResponse>> crearUsuario(
    @RequestBody Usuario usuario) {
    // ...
}
```

### ¿Cómo documento los parámetros?

```java
@GetMapping("/{id}")
public ResponseEntity<?> obtenerPorId(
    @io.swagger.v3.oas.annotations.Parameter(
        description = "ID del usuario a buscar",
        example = "1",
        required = true)
    @PathVariable Long id) {
    // ...
}
```

### ¿Cómo documento mis DTOs?

Usa `@Schema` en las clases y sus campos:

```java
@Data
@Schema(description = "Información del usuario")
public class UsuarioResponse {
    @Schema(description = "ID único del usuario", example = "1")
    private Long id;
    
    @Schema(description = "Nombre del usuario", example = "Juan Pérez")
    private String nombre;
}
```

### ¿Cómo documento un parámetro de query?

```java
@GetMapping("/buscar")
public ResponseEntity<?> buscar(
    @io.swagger.v3.oas.annotations.Parameter(
        description = "Nombre a buscar")
    @RequestParam String nombre) {
    // ...
}
```

---

## Solución de Problemas

### Swagger UI no carga / Página en blanco

**Problema**: Accedes a http://localhost:8080/swagger-ui.html pero ves una página en blanco

**Soluciones**:
1. Verifica que la aplicación esté ejecutándose: `http://localhost:8080`
2. Verifica que no haya errores en la consola
3. Limpia el caché: Ctrl+Shift+Delete (en navegadores)
4. Intenta incógnito: Ctrl+Shift+N

### Los endpoints no aparecen en Swagger

**Problema**: Tu controlador no aparece en Swagger UI

**Soluciones**:
1. Verifica que el controlador tenga `@RestController` o `@Controller`
2. Verifica que tenga `@RequestMapping` o que los métodos tengan `@GetMapping`, `@PostMapping`, etc.
3. Verifica que los imports de anotaciones sean correctos
4. Reinicia la aplicación después de cambios

### Obtengo error "Unauthorized" al probar

**Problema**: Swagger dice "No autorizado" al intentar probar un endpoint

**Soluciones**:
1. Los endpoints están protegidos por Spring Security
2. Primero debes autenticarte en `/api/v1/auth/login`
3. Algunos endpoints puede que requieran ciertos roles (ADMIN, USER, etc.)

### ¿Por qué no puedo probar ciertos endpoints?

**Razón**: Probablemente están protegidos por Spring Security

Para permitir que Swagger acceda a endpoints sin autenticación, puedes modificar `SecurityConfig.java`:

```java
.requestMatchers("/api/v1/protected").permitAll()  // Público
.requestMatchers("/api/v1/admin/**").hasRole("ADMIN")  // Solo ADMIN
```

---

## Configuración

### ¿Cómo cambio el título de la API en Swagger?

Modifica `OpenApiConfig.java`:

```java
.info(new Info()
    .title("Mi Nuevo Título")  // Aquí
    .description("Descripción")
    .version("1.0.0"))
```

### ¿Cómo cambio la descripción?

```java
.info(new Info()
    .title("Medilink API")
    .description("Tu nueva descripción aquí")  // Aquí
    .version("1.0.0"))
```

### ¿Cómo cambio la información de contacto?

```java
.contact(new Contact()
    .name("Tu Nombre")  // Aquí
    .email("tu@email.com")  // Aquí
    .url("https://tuwebsite.com"))  // Aquí
```

### ¿Cómo agregar autenticación en Swagger UI?

Esto requiere configuración adicional de OAuth 2.0 o API Key. Consulta la documentación de SpringDoc.

---

## Seguridad

### ¿Es seguro exponer Swagger UI?

**Depende de tu caso de uso:**
- ✅ **Sí es seguro** para desarrollo y testing interno
- ⚠️ **En producción**: Considera limitarlo a IPs específicas o deshabilitarlo
- ⚠️ **No expongas información sensible** en descripciones

### ¿Cómo deshabilito Swagger UI en producción?

En `application-prod.properties`:

```properties
# Deshabilitar Swagger UI
springdoc.swagger-ui.enabled=false
```

O en `application.yml`:

```yaml
springdoc:
  swagger-ui:
    enabled: false
```

### ¿Cómo restrinjo el acceso a Swagger UI?

En `SecurityConfig.java`:

```java
.requestMatchers("/swagger-ui/**", "/v3/api-docs/**")
    .hasRole("ADMIN")  // Solo admins ven Swagger
```

---

## Integración

### ¿Puedo generar un cliente con la especificación OpenAPI?

**Sí**, puedes usar herramientas como:
- **openapi-generator**: Genera clientes en Java, JavaScript, Python, etc.
- **swagger-codegen**: Herramienta similar de Swagger
- **StopLight**: IDE visual para especificaciones OpenAPI

Ejemplo con openapi-generator:
```bash
openapi-generator-cli generate \
  -i http://localhost:8080/v3/api-docs \
  -g javascript \
  -o ./generated-client
```

### ¿Cómo importo la especificación en Postman?

1. Abre Postman
2. Click en **"Import"** (esquina superior izquierda)
3. Selecciona **"Link"** tab
4. Pega: `http://localhost:8080/v3/api-docs`
5. Click en **"Import"**

Postman cargará automáticamente todos tus endpoints.

---

## Versioning

### ¿Cómo versiono mi API?

Opción 1: En la URL
```java
@RequestMapping("/api/v1/usuarios")  // v1
@RequestMapping("/api/v2/usuarios")  // v2
```

Opción 2: En headers
```java
@GetMapping(value = "/usuarios", headers = "X-API-Version=1")
```

### ¿Cómo muestro versiones diferentes en Swagger?

Crea múltiples `OpenApiConfig` o configura en `OpenApiConfig.java`:

```java
.info(new Info()
    .title("Medilink API")
    .version("1.0.0"))  // Cambia aquí para versiones
```

---

## Performance

### ¿Ralentiza Swagger UI el servidor?

**No significativamente**. Swagger UI:
- Se carga solo cuando lo visitas
- Es estático (no consume recursos del servidor)
- La especificación se genera una sola vez al iniciar

### ¿Puedo deshabilitar la generación de Swagger?

En `application.properties`:

```properties
springdoc.api-docs.enabled=false
springdoc.swagger-ui.enabled=false
```

---

## Actualización de Documentación

### La documentación no se actualiza después de cambios

**Solución**: Reinicia la aplicación

Spring Boot regenera la especificación al iniciar, pero **no** automáticamente durante ejecución.

### ¿Hay algún modo "watch" para recargar?

Con `spring-boot-devtools`, los cambios se detectan automáticamente:
- Recompila el código
- Reinicia la aplicación
- Accede a Swagger UI y verás los cambios

---

## Preguntas Avanzadas

### ¿Puedo agregar ejemplos de respuesta?

```java
@io.swagger.v3.oas.annotations.responses.ApiResponse(
    responseCode = "200",
    description = "Usuario encontrado",
    content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = UsuarioResponse.class),
        examples = @ExampleObject(value = "{ \"id\": 1, \"nombre\": \"Juan\" }")
    )
)
```

### ¿Cómo documento errores complejos?

Crea una clase de error y úsala en `@ApiResponses`:

```java
@Schema(description = "Respuesta de error detallada")
public class ErrorDetail {
    @Schema(example = "VALIDATION_ERROR")
    private String code;
    
    @Schema(example = "El email es inválido")
    private String message;
}
```

---

## Contacto y Soporte

Si tienes problemas con Swagger/OpenAPI en Medilink:

1. Revisa la [documentación oficial de SpringDoc](https://springdoc.org/)
2. Consulta el [archivo SWAGGER_SETUP.md](./SWAGGER_SETUP.md)
3. Revisa los [ejemplos de uso](./EJEMPLOS_SWAGGER.md)

---

**Última actualización**: 2 de junio de 2024
