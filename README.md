<h1 align="center">MEDILINK</h1>
<h3 align="center">Sistema Web de Gestión de Citas Médicas y Teleconsultas</h3>

<p align="center">
  <a href="https://sonarcloud.io/summary/new_code?id=ryzen-ry_Medilink"><img src="https://sonarcloud.io/api/project_badges/measure?project=ryzen-ry_Medilink&metric=alert_status" alt="Quality Gate Status"></a>
  <a href="https://sonarcloud.io/summary/new_code?id=ryzen-ry_Medilink"><img src="https://sonarcloud.io/api/project_badges/measure?project=ryzen-ry_Medilink&metric=coverage" alt="Coverage"></a>
  <a href="https://sonarcloud.io/summary/new_code?id=ryzen-ry_Medilink"><img src="https://sonarcloud.io/api/project_badges/measure?project=ryzen-ry_Medilink&metric=bugs" alt="Bugs"></a>
  <a href="https://sonarcloud.io/summary/new_code?id=ryzen-ry_Medilink"><img src="https://sonarcloud.io/api/project_badges/measure?project=ryzen-ry_Medilink&metric=vulnerabilities" alt="Vulnerabilities"></a>
  <a href="https://sonarcloud.io/summary/new_code?id=ryzen-ry_Medilink"><img src="https://sonarcloud.io/api/project_badges/measure?project=ryzen-ry_Medilink&metric=code_smells" alt="Code Smells"></a>
  <a href="https://sonarcloud.io/summary/new_code?id=ryzen-ry_Medilink"><img src="https://sonarcloud.io/api/project_badges/measure?project=ryzen-ry_Medilink&metric=duplicated_lines_density" alt="Duplication"></a>
</p>

🌐 **Demo en producción:** [medilink-production-8cf3.up.railway.app](https://medilink-production-9950.up.railway.app/)

---

## Información General
| | |
|---|---|
| **Título** | Sistema Web de Gestión de Citas Médicas y Teleconsultas "MEDILINK" |
| **Universidad** | Universidad Tecnológica del Perú |
| **Curso** | Herramientas de Desarrollo |
| **Docente** | Milla Flores, José Luis |
| **Sección** | 23280 — Grupo 6 |
| **Integrantes** | Antiquera Farfán, Ronaldo Isaac · Campos Toribio, Liliana Lilibeth · Adrián Ore, Luis Francisco |
| **Fecha de inicio** | 15/04/2026 |
| **Fecha de finalización** | 20/07/2026 |


## Agradecimientos

Expresamos nuestro más sincero agradecimiento al Ing. José Luis Milla Flores, nuestro estimado docente del curso Herramientas de Desarrollo, por su invaluable enseñanza, paciencia, dedicación y por compartir con nosotros no solo los fundamentos técnicos del desarrollo web sino también la importancia de la calidad, la ética profesional y el trabajo bien hecho, siendo su guía fundamental para la culminación exitosa de este proyecto. Asimismo, agradecemos a nuestros compañeros de equipo por su compromiso, trabajo en equipo y colaboración constante, a la clínica MediLink por permitirnos tomar su realidad como caso de estudio, y finalmente a nuestras familias y amigos, quienes nos brindaron su apoyo emocional y comprensión durante las largas jornadas de trabajo.

## Dedicatoria

Este proyecto está dedicado principalmente a nuestros padres, por su apoyo incondicional, sacrificio y por ser el pilar fundamental en nuestra formación personal y profesional, así como a nuestros docentes, quienes con paciencia y vocación compartieron sus conocimientos y nos guiaron en el camino del desarrollo tecnológico con responsabilidad y ética. Y especialmente a todos los profesionales de la salud y pacientes de zonas rurales del Perú, quienes enfrentan día a día las barreras de acceso a la atención médica, para que este pequeño aporte tecnológico sea un paso hacia una salud más justa, accesible y humana.

---

## Resumen

El presente proyecto consiste en el desarrollo del Sistema Web de Gestión de Citas Médicas y Teleconsultas "MEDILINK", una plataforma de telemedicina diseñada para optimizar la gestión de consultas médicas y mejorar el acceso a la atención sanitaria, especialmente en zonas rurales del Perú. La problemática principal abordada es el registro manual de citas, la pérdida de información y la limitada capacidad de respuesta ante la creciente demanda de servicios de salud. Para su implementación se utilizó una arquitectura moderna basada en Spring Boot bajo el patrón Modelo-Vista-Controlador (MVC), empleando Spring Data JPA e Hibernate para la gestión de la base de datos relacional, Thymeleaf como motor de plantillas para las vistas dinámicas, y Spring Security con autenticación basada en roles para garantizar la confidencialidad e integridad de los datos clínicos. Como resultado se obtuvo un sistema funcional, seguro y escalable que automatiza el registro de pacientes y citas, reduce errores administrativos y ofrece una experiencia de usuario accesible desde cualquier navegador web.

**Palabras clave:** Telemedicina, Spring Boot, MVC, seguridad digital, gestión de citas médicas, MediLink.

## Abstract

This project consists of the development of the "MEDILINK" Web System for Medical Appointment Management and Teleconsultations, a telemedicine platform designed to optimize medical consultation management and improve access to healthcare, especially in rural areas of Peru. The main problem addressed is manual appointment registration, loss of information, and limited responsiveness to the growing demand for health services. For its implementation, a modern architecture based on Spring Boot was used under the Model-View-Controller (MVC) pattern, employing Spring Data JPA and Hibernate for relational database management, Thymeleaf as a template engine for dynamic views, and Spring Security with role-based authentication to guarantee the confidentiality and integrity of clinical data. As a result, a functional, secure and scalable system was obtained that automates patient and appointment registration, reduces administrative errors, and offers an accessible user experience from any web browser.

**Keywords:** Telemedicine, Spring Boot, MVC, digital security, medical appointment management, MediLink.

---

## Realidad Problemática

El sistema de salud en el Perú enfrenta un grave desafío de equidad en la atención, afectando principalmente a la población de bajos recursos y a quienes residen en zonas rurales. Según el Instituto Nacional de Estadística e Informática (INEI), en el cuarto trimestre del 2023 solo el 47,0 % de las personas que manifestaron algún problema de salud accedieron a un servicio médico, mientras que el 53,0 % no buscó atención. La diferencia entre ámbitos es notoria: en áreas rurales la cifra fue de apenas 42,5 %, frente al 48,6 % en zonas urbanas, lo cual evidencia una profunda desigualdad territorial.

La clínica MediLink, dedicada a brindar servicios de atención médica, actualmente enfrenta dificultades en la gestión de sus consultas porque el registro de estas se realiza de manera manual. Este método provoca varios problemas como la pérdida de información, duplicidad de datos, retrasos en la atención y dificultades al momento de realizar un seguimiento a un paciente. A ello se suma la concentración de profesionales de la salud en las principales ciudades, lo que intensifica la saturación de los hospitales urbanos y deja a muchas provincias sin especialistas suficientes.

## Justificación

La implementación del Sistema Web de Gestión de Citas Médicas y Teleconsultas "MediLink" es una respuesta estratégica e imperativa a las limitaciones tecnológicas y las demandas actuales del sector salud. El proyecto se sustenta en una arquitectura robusta y moderna, utilizando Spring Boot para construir un backend modular, escalable y optimizado. Las interfaces dinámicas, desarrolladas con HTML, CSS, JavaScript y Bootstrap, aseguran una experiencia de usuario (UX) intuitiva y adaptable. La seguridad digital se aborda mediante Spring Security, que permite gestionar la autenticación y autorización de usuarios, el control de acceso por roles y la protección de datos sensibles.

En un contexto global, la Organización Mundial de la Salud (OMS, 2020) ha señalado que la telemedicina puede desempeñar un papel fundamental en la mejora del acceso a los servicios de salud, especialmente en situaciones en las que la distancia y el tiempo son factores críticos.

---

## Objetivo General

Desarrollar una plataforma web de telemedicina que permita realizar consultas médicas en tiempo real, gestionar datos clínicos y garantizar la confidencialidad de la información.

## Objetivos Específicos

- Implementar una arquitectura web moderna utilizando Spring Boot y Spring Web, con optimización del código y automatización de procesos dentro del entorno backend.
- Desarrollar el módulo de atención médica en tiempo real implementando WebRTC para videollamadas, y aplicando interfaces dinámicas con HTML, CSS, JavaScript y Bootstrap.
- Operacionalizar mecanismos robustos de seguridad digital mediante Spring Security, autenticación basada en JWT, validaciones con Hibernate Validator y control de roles y permisos.

---

## Marco Teórico y Metodología

### Bases teóricas

- **Transformación digital:** la integración de tecnologías digitales para rediseñar procesos, servicios y modelos organizacionales (Brynjolfsson y McAfee, 2014). En MediLink sustenta la migración de la atención manual hacia un ecosistema digital.
- **Telemedicina:** definida por la OMS como la prestación de servicios sanitarios a distancia mediante tecnologías de información y comunicación, permitiendo consultas remotas, seguimiento clínico y reducción de barreras geográficas.
- **Arquitectura de software escalable:** una arquitectura en capas (controlador, servicio, repositorio), propia de Spring Boot, que separa responsabilidades y permite escalar los módulos más demandados, como la gestión de citas (Richardson, 2018).
- **Metodologías ágiles:** basadas en el Manifiesto Ágil (Beck et al., 2001), priorizan la colaboración, la entrega incremental y la respuesta al cambio.
- **Seguridad de la información:** la tríada CIA (confidencialidad, integridad, disponibilidad) y la norma ISO/IEC 27001 respaldan el uso de Spring Security, JWT y control de roles.
- **Bases de datos relacionales:** el modelo relacional (Codd, 1970) organiza la información en tablas vinculadas por claves primarias y foráneas; MediLink usa MySQL para modelar usuarios, roles, citas y especialidades.

### Metodología: Scrum

Para el desarrollo de MediLink se seleccionó la metodología ágil **Scrum**, debido a la necesidad de construir un sistema complejo, con altos requerimientos de seguridad, donde la flexibilidad y la entrega incremental son claves.

| Necesidad del proyecto | Beneficio de Scrum |
|---|---|
| Requisitos cambiantes | Los sprints cortos permiten adaptar el backlog cada 2-4 semanas. |
| Alta calidad y seguridad | Las revisiones al final de cada sprint aseguran los estándares de calidad (OE3). |
| Colaboración constante | Fomenta la comunicación diaria y la auto-organización del equipo. |
| Feedback temprano | El Sprint Review permite mostrar el producto funcional y recibir retroalimentación. |

**Artefactos:** Product Backlog e Issues en GitHub Projects, Sprint Backlog (columna "Sprint Actual"), e Incrementos entregables al final de cada sprint.

---

## Cronograma de Actividades (Diagrama de Gantt)

<!-- Reemplaza la ruta de abajo por la imagen real que está en resultados/capturas_pantalla/ -->

El cronograma se organizó en tres sprints principales: **Sprint 0** (configuración del proyecto y conexión a base de datos), **Sprint 1** (autenticación y seguridad con JWT) y **Sprint 2** (gestión de citas y paneles de médico/paciente), alineados con los hitos de evaluación del curso (Hito 1 a Hito 4).

---

## Desarrollo del Proyecto

### Aplicación

El backend se desarrolló en **Java con Spring Boot**, siguiendo una arquitectura en capas (`controller` → `service` → `repository`). Se implementaron los módulos de autenticación (`/api/auth/login`, `/api/auth/registro`, `/api/auth/logout`) con JWT y `BCryptPasswordEncoder`, y el módulo de gestión de citas (`CitaController`, `CitaService`, `CitaRepository`) con operaciones CRUD, validación de disponibilidad de horarios y búsqueda de médicos por especialidad. Las vistas se construyeron con **Thymeleaf**, **Bootstrap** y **Materialize CSS** para los paneles de paciente y médico.

La calidad del código se valida automáticamente en cada `push` mediante un flujo de **GitHub Actions** que ejecuta `mvn clean test` y un análisis de **SonarCloud** (bugs, vulnerabilidades, code smells y duplicación — ver badges al inicio de este documento). La aplicación se empaqueta con un **Dockerfile multi-etapa** (compilación con Maven, ejecución con JRE) y un `docker-compose.yml` que integra la app y la base de datos. El despliegue final se realiza en **Railway**.

### Base de Datos

Se utilizó **MySQL** como motor relacional. El modelo entidad-relación contempla las entidades `Usuario`, `Rol`, `Cita`, `Especialidad`, `HorarioDisponible` y `Doctor`, relacionadas mediante claves foráneas para garantizar la integridad referencial. Los diagramas conceptual, entidad-relación y físico se encuentran documentados en `docs/` y en el informe final (`informes/`).

---

## Resultados

- Backend funcional en Spring Boot con autenticación JWT y control de roles (administrador, médico, paciente).
- Módulo de gestión de citas médicas operativo: registro, consulta, cancelación y validación de disponibilidad.
- Flujo de integración continua (CI) en GitHub Actions con compilaciones y pruebas exitosas en cada push.
- Análisis de calidad de código integrado con SonarCloud (ver badges de Quality Gate, Bugs, Vulnerabilities y Coverage al inicio de este README).
- Aplicación empaquetada en Docker (multi-etapa) y verificada localmente con `docker-compose`.
- Despliegue en producción activo en Railway: **https://medilink-production-9950.up.railway.app/**
- Flujo de trabajo colaborativo con ramas protegidas (`main`, `develop`), revisiones obligatorias de Pull Requests y tablero Kanban en GitHub Projects.
- 4 Releases publicados en GitHub, desde la plantilla base (`1.0`) hasta la versión operativa actual (`1.3`).

---

## Estructura del Repositorio

```
Medilink/
├── README.md
├── .gitignore
├── LICENSE
├── docs/                  # Documentación técnica (requisitos, resolución de conflictos, despliegue)
├── src/                   # Código fuente (Spring Boot)
├── informes/              # Informes formales en PDF
├── resultados/            # Evidencias visuales y reportes (resumen_ci.md, capturas)
├── Dockerfile             # Imagen multi-etapa de la aplicación
└── docker-compose.yml     # Orquestación de la app y la base de datos MySQL
```

## Cómo Ejecutar el Proyecto Localmente

```bash
# Clonar el repositorio
git clone https://github.com/ryzen-ry/Medilink.git
cd Medilink

# Levantar la aplicación y la base de datos con Docker
docker-compose up -d

# Verificar que los contenedores estén activos
docker ps
```

La aplicación quedará disponible en `http://localhost:8080`.

## Licencia

Este proyecto se distribuye bajo la licencia [Apache-2.0](LICENSE).
