FROM maven:3.8.5-openjdk-17 AS build

# Copiamos el código fuente de tu proyecto al contenedor
COPY . .

# Ejecutamos el comando para compilar y crear el .jar (saltando los tests para ahorrar tiempo)
RUN mvn clean package -DskipTests

# --- Etapa 2: Ejecución (Run) ---
# Usamos una imagen ligera de Java 17 solo para correr la app
FROM openjdk:17-jdk-slim

# Copiamos el .jar generado en la etapa anterior
# OJO: Asegúrate de que el nombre coincida con el de tu pom.xml
COPY --from=build /target/MonoPat-0.0.1-SNAPSHOT.jar app.jar

# Exponemos el puerto 8080
EXPOSE 8080

# Comando para iniciar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]