# ================================
# Etapa 1: Construcción del JAR
# ================================
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Directorio de trabajo
WORKDIR /app

# Copiar archivos de configuración primero (cachear dependencias)
COPY pom.xml ./
COPY mvnw ./
COPY .mvn .mvn

# Descargar dependencias sin compilar
RUN ./mvnw dependency:go-offline -B

# Copiar el resto del código fuente
COPY src ./src

# Compilar, ejecutar tests y empaquetar (versión producción)
RUN ./mvnw clean package -Pprod

# ================================
# Etapa 2: Imagen final ligera
# ================================
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copiar solo el JAR generado
COPY --from=build /app/target/*.jar app.jar

# Puerto expuesto (Spring Boot por defecto)
EXPOSE 8080

# Ejecutar aplicación
ENTRYPOINT ["java","-jar","app.jar"]