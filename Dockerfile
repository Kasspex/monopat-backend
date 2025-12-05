# --- Etapa 1: Construcción (Build) ---
# Usamos una imagen de Maven basada en Eclipse Temurin (más estable)
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Copiamos el código fuente
COPY . .

# Compilamos el proyecto y generamos el .jar
RUN mvn clean package -DskipTests

# --- Etapa 2: Ejecución (Run) ---
# Usamos Eclipse Temurin Alpine (muy ligera y compatible)
FROM eclipse-temurin:17-jdk-alpine

# Copiamos el .jar generado en la etapa anterior
# IMPORTANTE: Asegúrate de que este nombre coincida con el artifactId y version de tu pom.xml
COPY --from=build /target/MonoPat-0.0.1-SNAPSHOT.jar app.jar

# Exponemos el puerto 8080
EXPOSE 8080

# Comando de inicio
ENTRYPOINT ["java", "-jar", "app.jar"]
