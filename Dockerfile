FROM openjdk:17
WORKDIR /app
COPY target/student-management-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "app.jar"]