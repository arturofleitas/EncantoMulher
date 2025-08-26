# Usar una imagen base oficial de Java
FROM openjdk:17-jdk-slim

# Exponer el puerto que usa tu aplicación
EXPOSE 8080

# Agregar el archivo JAR de tu aplicación al contenedor
ADD target/encantomulher-1.0.0.jar app.jar

# Ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]