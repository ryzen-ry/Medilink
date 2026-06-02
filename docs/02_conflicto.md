# Registro de Conflictos de Merge - MediLink

## Fecha: [fecha actual]
## Participantes: ryzen-ry y lilibeth-17
## Rama: feature/AreglosFinales vs feature/dasboar-estilonuevo

---

## Conflicto #1: CitaService.java

### Archivo:
`src/main/java/com/medilink/service/CitaService.java`

### Descripción del Conflicto:
Se produjo un conflicto porque ambos desarrolladores modificaron el mismo método `agendarCita()` en diferentes ramas.

### Cambios de mi compañero:
```java
// Método modificado por mi compañero
public Cita agendarCita(CitaDTO citaDTO) {
    // Validación de horario
    if (!validarHorarioLaboral(citaDTO.getFechaHora())) {
        throw new IllegalArgumentException("Horario fuera de atención");
    }
    // Lógica existente...
    return citaRepository.save(cita);
}
## Conflicto #2: dashboard.html
### Archivo:
`src/main/resources/templates/ADMIN/dashboard.html`

### Descripción del Conflicto:
Se trabajó sobre la **nueva versión** del dashboard que incluye sidebar lateral, diseño moderno con variables CSS, y componentes personalizados. Ambos desarrolladores modificaron secciones diferentes pero superpuestas.

### Cambios de mi compañero:
- Modificó la sección de **Doctores** agregando una columna de "Horario de atención"
- Añadió validaciones JavaScript en el frontend
- Cambió los colores del sidebar y cards

### Mis cambios:
- Modifiqué la sección de **Usuarios** agregando filtros de búsqueda
- Añadí botones de "Editar" en la tabla de doctores
- Corregí el responsive para móviles (media queries)

### Resolución Aplicada:

**Decisión:** Mantener ambas funcionalidades y unificar el diseño.

**Cambios finales combinados:**
1. ✅ Se mantuvo la columna "Horario de atención" en Doctores (de compañero)
2. ✅ Se agregaron los filtros de búsqueda en Usuarios (míos)
3. ✅ Se unificaron los estilos CSS manteniendo las variables de ambos
4. ✅ Se conservaron ambos botones: "Editar" (mío) y "Eliminar" (de él)
5. ✅ Se mejoró el responsive combinando ambas media queries

al final se opto por conservar los cambios originales  al solucionar el conflicto se hizo la verificacion 
