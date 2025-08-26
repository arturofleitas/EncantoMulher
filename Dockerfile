# ETAPA 1: BUILD (Compilación)
# Usamos una imagen de Maven para compilar el proyecto
FROM maven:3.8.5-openjdk-17 AS builder

# Establecer el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia los archivos del proyecto (pom.xml y el código fuente) al contenedor
COPY . .

# Compila la aplicación con Maven
# La opción `-DskipTests` salta las pruebas para que la compilación sea más rápida
# La opción `package` empaqueta la aplicación en un archivo JAR
RUN mvn clean package -DskipTests

# ETAPA 2: RUNTIME (Ejecución)
# Usamos una imagen ligera de OpenJDK para ejecutar la aplicación
# Esto reduce el tamaño final del contenedor, haciéndolo más eficiente
FROM openjdk:17-jdk-slim

# Expone el puerto por defecto de Spring Boot
EXPOSE 8080

# Copia el archivo JAR compilado desde la etapa "builder"
# La ruta es relativa al WORKDIR de la etapa "builder"
COPY --from=builder /app/target/encantomulher-1.0.0.jar app.jar

# Ejecuta la aplicación cuando se inicie el contenedor
ENTRYPOINT ["java", "-jar", "/app.jar"]