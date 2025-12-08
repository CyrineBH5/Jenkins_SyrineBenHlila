# Dockerfile correct pour Spring Boot
FROM eclipse-temurin:17-jre-alpine

# Définir le répertoire de travail
WORKDIR /app

# Copier le fichier JAR
COPY target/student-management-0.0.1-SNAPSHOT.jar app.jar

# Exposer le port (Spring Boot utilise 8080 par défaut)
EXPOSE 8081

# Commande pour exécuter l'application
ENTRYPOINT ["java", "-jar", "app.jar"]