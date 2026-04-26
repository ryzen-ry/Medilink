# Medilink

titulo: Sistema Web de Gestión de Citas Médicas y Teleconsultas "MEDILINK"

integrantes: -antiquera farfan ronaldi isacc u22208906 -Campos Toribia Liliana lilibeth

docente: Milla Flores Jose Luis

Universidad: Universidad Tecnologica del Peru

Fecha de inicio y Finalizacion: 15/04/2026 -- 30/07/2026

agradecimientos y dedicatoria AGRADECIMIENTOS Expresamos nuestro más sincero agradecimiento al ING. Jose Luis Milla Flores , nuestro estimado docente del curso Herramientas de Desarrollo Web, por su invaluable enseñanza, paciencia, dedicación y por compartir con nosotros no solo los fundamentos técnicos del desarrollo web sino también la importancia de la calidad, la ética profesional y el trabajo bien hecho, siendo su guía fundamental para la culminación exitosa de este proyecto; asimismo, agradecemos a nuestros compañeros de grupo –Ronaldo Antiquera, Eddy Timoteo, Christian Rivas y Karolay Huamani– por su compromiso, trabajo en equipo y colaboración constante, a la clínica MediLink por permitirnos tomar su realidad como caso de estudio, y finalmente a nuestras familias y amigos, quienes nos brindaron su apoyo emocional y comprensión durante las largas jornadas de trabajo.

DEDICATORIA Este proyecto está dedicado principalmente a nuestros padres, por su apoyo incondicional, sacrificio y por ser el pilar fundamental en nuestra formación personal y profesional, así como a nuestros docentes, quienes con paciencia y vocación compartieron sus conocimientos y nos guiaron en el camino del desarrollo tecnológico con responsabilidad y ética, y especialmente a todos los profesionales de la salud y pacientes de zonas rurales del Perú, quienes enfrentan día a día las barreras de acceso a la atención médica, para que este pequeño aporte tecnológico sea un paso hacia una salud más justa, accesible y humana.

Resumen y Abstract: Resumen El presente proyecto consiste en el desarrollo del Sistema Web de Gestión de Citas Médicas y Teleconsultas "MEDILINK", una plataforma de telemedicina diseñada para optimizar la gestión de consultas médicas y mejorar el acceso a la atención sanitaria, especialmente en zonas rurales del Perú. La problemática principal abordada es el registro manual de citas, la pérdida de información y la limitada capacidad de respuesta ante la creciente demanda de servicios de salud. Para su implementación, se utilizó una arquitectura moderna basada en Spring Boot bajo el patrón Modelo–Vista–Controlador (MVC), empleando Spring Data JPA e Hibernate para la gestión de la base de datos relacional, Thymeleaf como motor de plantillas para las vistas dinámicas y Spring Security con autenticación basada en roles para garantizar la confidencialidad e integridad de los datos clínicos. Como resultado, se obtuvo un sistema funcional, seguro y escalable que automatiza el registro de pacientes y citas, reduce errores administrativos y ofrece una experiencia de usuario accesible desde cualquier navegador web. Se concluye que MEDILINK constituye una solución tecnológica viable para transformar la atención médica tradicional hacia un modelo digital eficiente.

Palabras clave: Telemedicina, Spring Boot, MVC, seguridad digital, gestión de citas médicas, MEDILINK.

Abstract This project consists of the development of the "MEDILINK" Web System for Medical Appointment Management and Teleconsultations, a telemedicine platform designed to optimize medical consultation management and improve access to healthcare, especially in rural areas of Peru. The main problem addressed is manual appointment registration, loss of information, and limited responsiveness to the growing demand for health services. For its implementation, a modern architecture based on Spring Boot was used under the Model-View-Controller (MVC) pattern, employing Spring Data JPA and Hibernate for relational database management, Thymeleaf as a template engine for dynamic views, and Spring Security with role-based authentication to guarantee the confidentiality and integrity of clinical data. As a result, a functional, secure and scalable system was obtained that automates patient and appointment registration, reduces administrative errors, and offers an accessible user experience from any web browser. It is concluded that MEDILINK constitutes a viable technological solution to transform traditional medical care into an efficient digital model.

Keywords: Telemedicine, Spring Boot, MVC, digital security, medical appointment management, MEDILINK.

Realidad Problematica y Justificacion del Proyecto

Realidad Problemática El sistema de salud en el Perú enfrenta un grave desafío de equidad en la atención, afectando principalmente a la población de bajos recursos y a quienes residen en zonas rurales. Según el Instituto Nacional de Estadística e Informática (INEI), en el cuarto trimestre del 2023, solo el 47,0 % de las personas que manifestaron algún problema de salud accedieron a un servicio médico, mientras que el 53,0 % no buscó atención. La diferencia entre ámbitos es notoria: en áreas rurales la cifra fue de apenas 42,5 %, frente al 48,6 % en zonas urbanas, lo cual evidencia una profunda desigualdad territorial.La clínica MediLink, dedicada a brindar servicios de atención médica, actualmente enfrenta dificultadesen la gestión de sus consultas porque el registro de estas se realiza de manera manual. Este métodoprovoca varios problemas como la pérdida de información, duplicidad de datos, retrasos en la atención y dificultades al momento de realizar un seguimiento a un paciente. La falta de un sistema digital eficiente no solo afecta la administración interna, sino también la calidad del servicio que recibenlos pacientes, generando insatisfacción y limitando la capacidad de respuesta de la clínica frente a una creciente demanda de atención médica. A ello se suma la concentración de profesionales de la saluden las principales ciudades, lo que intensifica la saturación de los hospitales urbanos y deja a muchasprovincias sin especialistas suficientes. Ante esta realidad, surge la necesidad de explorar solucionesinnovadoras que trasciendan las limitaciones físicas del sistema sanitario, siendo MediLink una alternativaviable para ampliar el acceso a consultas médicas seguras y oportunas.

Justificación del Proyecto La implementación del Sistema Web de Gestión de Citas Médicas y Teleconsultas "MEDILINK" es una respuesta estratégica e imperativa a las limitaciones tecnológicas y las demandas actuales del sector salud. La clínica MediLink enfrenta barreras significativas, incluyendo la discontinuidad del tratamiento, la vulnerabilidad en la seguridad de los datos clínicos y la lentitud en la comunicación médico-paciente. Este proyecto no solo resuelve estos problemas, sino que también posiciona a la institución en la vanguardia de la transformación digital sanitaria. El proyecto se sustenta en una arquitectura robusta y moderna, utilizando Spring Boot para construir un backend modular, escalable y optimizado, que facilita el mantenimiento y la automatización de procesos. Las interfaces dinámicas, desarrolladas con HTML, CSS, JavaScript y Bootstrap, aseguran una experiencia de usuario (UX) intuitiva y adaptable, crucial para la accesibilidad de pacientes y la eficiencia de los profesionales de la salud. La seguridad digital es crítica y se aborda mediante Spring Security, que permite gestionar la autenticación y autorización de usuarios, el control de acceso por roles y la protección de datos sensibles, asegurando la integridad y privacidad de la información clínica. En un contexto global, la Organización Mundial de la Salud (OMS, 2020) ha señalado que "la telemedicina puede desempeñar un papel fundamental en la mejora del acceso a los servicios de salud, especialmente en situaciones en las que la distancia y el tiempo son factores críticos". Por ello, MediLink busca transformar el modelo de atención de la clínica, superando las limitaciones de los sistemas manuales y contribuyendo a cerrar la brecha de acceso a la salud en el Perú.

Objetivo general y Especificos

Objetivo general

Desarrollar una plataforma web de telemedicina, que permita realizar consultas médicas en tiempo real, gestionar datos clínicos y garantizar la confidencialidad de la información.

Objetivos Especificos

-Implementar una arquitectura web moderna utilizando Spring Boot, Spring Web, optimización del código y automatización de procesos dentro del entorno backend.

-Desarrollar el módulo de atención médica en tiempo real implementando WebRTC para videollamadas, y aplicando interfaces dinámicas con HTML, CSS, JavaScript y Bootstrap, permitiendo la comunicación fluida entre médicos y pacientes.

-Operacionalizar mecanismos robustos de seguridad digital mediante Spring Security, autenticación basada en JWT, validaciones con Hibernate Validator y control de roles y permisos

Marco teorico y Metodologia
