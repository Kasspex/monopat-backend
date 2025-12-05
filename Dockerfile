# --- Etapa 1: Construccion (Build) ---
# Usamos una imagen de Maven basada en Eclipse Temurin (mas estable)
FROM maven:3.9-eclipse-temurin-21-alpine AS build

# Copiamos el código fuente
COPY . .

# Compilamos el proyecto y generamos el .jar
RUN mvn clean package -Dmaven.test.skip=true

# --- Etapa 2: Ejecucion (Run) ---
# Usamos Eclipse Temurin Alpine (muy ligera y compatible)
FROM eclipse-temurin:21-jdk-alpine

# Copiamos el .jar generado en la etapa anterior
# IMPORTANTE: Asegurate de que este nombre coincida con el artifactId y version de tu pom.xml
COPY --from=build /target/MonoPat-0.0.1-SNAPSHOT.jar app.jar

# Exponemos el puerto 8080
EXPOSE 8080

# Comando de inicio
ENTRYPOINT ["java", "-jar", "app.jar"]
