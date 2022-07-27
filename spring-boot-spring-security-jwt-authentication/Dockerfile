#### Stage 1: Build the application
FROM openjdk:17-jdk as build


ADD /target/spring-boot-security-jwt-0.0.1-SNAPSHOT.jar spring-boot-security-jwt-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "spring-boot-security-jwt-0.0.1-SNAPSHOT.jar"]