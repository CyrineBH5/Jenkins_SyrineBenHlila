# Utilise l'image Alpine avec Java 17 JRE (plus léger)
FROM eclipse-temurin:17-jre-alpine

# Définit le répertoire de travail dans le conteneur
WORKDIR /app

# Copie le fichier JAR de l'application
COPY target/student-management-0.0.1-SNAPSHOT.jar app.jar

# Expose le port de l'application
EXPOSE 8081

# Commande pour exécuter l'application
ENTRYPOINT ["java", "-jar", "app.jar"]