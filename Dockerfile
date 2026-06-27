# Build stage - compila la aplicación
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /workspace

# Copia pom.xml para cachear dependencias
COPY pom.xml ./
RUN mvn -B dependency:go-offline

# Copia el código y compila (sin ejecutar pruebas para acelerar)
COPY src ./src
RUN mvn -B -Dmaven.test.skip=true package

# Runtime stage - imagen liviana solo para ejecutar el JAR
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copia el JAR generado desde la etapa de build
COPY --from=build /workspace/target/medilink-0.0.1-SNAPSHOT.jar ./app.jar

# Puerto de la aplicación (Spring Boot por defecto)
EXPOSE 8080

# Ejecuta la aplicación
ENTRYPOINT ["java", "-jar", "/app/app.jar"]