# Integración de Swagger/OpenAPI en Medilink

## ¿Qué se ha implementado?

Se ha integrado **SpringDoc OpenAPI** para documentar automáticamente todos los endpoints REST del proyecto Medilink mediante Swagger UI.

## Cambios realizados

### 1. **Dependencia agregada (pom.xml)**
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.6.0</version>
</dependency>
```

### 2. **Configuración de Seguridad (SecurityConfig.java)**
Se agregaron rutas públicas para Swagger:
- `/v3/api-docs/**` - Especificación OpenAPI
- `/swagger-ui/**` - Interfaz de usuario de Swagger
- `/swagger-ui.html` - Página principal de Swagger

### 3. **Configuración OpenAPI (OpenApiConfig.java)**
Se creó una clase de configuración que define:
- Título: "Medilink API"
- Descripción de la API
- Versión: "1.0.0"
- Información de contacto
- Información de licencia

### 4. **Anotaciones en Controladores**
Se agregaron anotaciones OpenAPI/Swagger a todos los controladores REST:

#### AuthRestController
- `@Tag(name = "Autenticación", description = "Endpoints de autenticación de usuarios")`
- `@Operation` en cada método con descripción
- `@ApiResponses` con documentación de códigos HTTP

#### UsuarioRestController
- Documentación de todos los endpoints CRUD de usuarios
- Documentación de parámetros con `@Parameter`
- Documentación de solicitudes con `@RequestBody`

#### DoctorRestController
- Documentación completa de endpoints de doctores

#### CitaRestController
- Documentación completa de endpoints de citas

### 5. **Anotaciones en DTOs**
Se agregó `@Schema` a todas las clases de respuesta:
- `LoginDTO` - DTO de autenticación
- `ApiResponse<T>` - Envoltorio genérico de respuestas
- `UsuarioResponse` - Información de usuarios
- `DoctorResponse` - Información de doctores
- `CitaResponse` - Información de citas

## Cómo usar Swagger UI

### 1. **Iniciar la aplicación**
```bash
# Asegúrate de tener Java 21 instalado
mvn clean spring-boot:run
```

### 2. **Acceder a Swagger UI**
Una vez que la aplicación esté ejecutándose, abre tu navegador y ve a:

```
http://localhost:8080/swagger-ui.html
```

### 3. **Explorar los endpoints**
En Swagger UI podrás:
- Ver todos los endpoints disponibles organizados por etiquetas (Autenticación, Usuarios, Doctores, Citas)
- Ver la descripción de cada endpoint
- Ver los parámetros requeridos
- Ver los códigos de respuesta posibles
- Probar los endpoints directamente desde la interfaz
- Ver los esquemas de datos (DTOs)

## Endpoint de especificación OpenAPI

Si necesitas acceder a la especificación OpenAPI en formato JSON:

```
http://localhost:8080/v3/api-docs
```

## Ejemplos de uso en Swagger UI

### 1. **Autenticación**
1. Expande la sección "Autenticación"
2. Haz clic en `POST /api/v1/auth/login`
3. Haz clic en "Try it out"
4. Ingresa un email y contraseña válidos
5. Haz clic en "Execute"

### 2. **Crear un usuario**
1. Expande la sección "Usuarios"
2. Haz clic en `POST /api/v1/usuarios/registro`
3. Haz clic en "Try it out"
4. Completa el JSON con los datos del usuario
5. Haz clic en "Execute"

### 3. **Ver todos los doctores**
1. Expande la sección "Doctores"
2. Haz clic en `GET /api/v1/doctores`
3. Haz clic en "Execute"

## Estructura de respuestas

Todas las respuestas de la API siguen el formato `ApiResponse<T>`:

```json
{
  "success": true,
  "message": "Operación completada exitosamente",
  "data": {
    // Los datos específicos del endpoint
  },
  "timestamp": "2024-06-02T10:30:00"
}
```

### Respuesta exitosa (200 OK)
```json
{
  "success": true,
  "message": "Login exitoso",
  "data": {
    "id": 1,
    "email": "usuario@ejemplo.com",
    "nombre": "Juan Pérez",
    "rol": "USER",
    "sessionId": "ABC123"
  },
  "timestamp": "2024-06-02T10:30:00"
}
```

### Respuesta de error (4xx)
```json
{
  "success": false,
  "message": "Correo o contraseña incorrectos",
  "data": null,
  "timestamp": "2024-06-02T10:30:00"
}
```

## Tags disponibles

En Swagger UI verás los siguientes grupos de endpoints:

| Tag | Descripción |
|-----|-------------|
| **Autenticación** | Login, logout, obtener usuario autenticado |
| **Usuarios** | Crear, leer, actualizar, eliminar usuarios |
| **Doctores** | Gestión de doctores (CRUD) |
| **Citas** | Gestión de citas médicas |

## Seguridad

- Swagger UI está disponible públicamente para facilitar la exploración de la API
- Sin embargo, los endpoints requieren autenticación donde corresponde
- Todos los endpoints sensibles están protegidos por Spring Security

## Próximas mejoras opcionales

1. **Autenticación en Swagger**: Configurar OAuth 2.0 para autenticar en Swagger UI
2. **Documentación de errores**: Agregar más detalles a las respuestas de error
3. **Esquemas personalizados**: Definir más ejemplos en los DTOs
4. **Integración con generadores de clientes**: Usar la especificación OpenAPI para generar clientes en JavaScript, Python, etc.

## Verificación

Para verificar que Swagger está funcionando correctamente:

1. ✅ Accede a `http://localhost:8080/swagger-ui.html`
2. ✅ Deberías ver una interfaz interactiva con todos los endpoints
3. ✅ Cada endpoint debe mostrar: descripción, parámetros, códigos de respuesta
4. ✅ Deberías poder hacer "Try it out" en cualquier endpoint

## Documentación adicional

- [SpringDoc OpenAPI - Documentación oficial](https://springdoc.org/)
- [Swagger UI - Documentación oficial](https://swagger.io/tools/swagger-ui/)
- [OpenAPI 3.0 Specification](https://spec.openapis.org/oas/v3.0.3)

---

**Nota**: La integración de Swagger/OpenAPI es completamente transparente. Los endpoints funcionan exactamente igual, solo que ahora están documentados automáticamente.
