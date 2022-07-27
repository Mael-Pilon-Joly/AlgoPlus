#### Stage 1: Build the application
FROM maven:3.8.3-openjdk:17-jdk AS builder
COPY pom.xml /app/
COPY src /app/src
RUN --mount=type=cache,target=/root/.m2 mvn -f /app/pom.xml clean package -DskipTests

COPY --from=builder /app/target/spring-boot-security-jwt-0.0.1-SNAPSHOT.jar  spring-boot-security-jwt-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "spring-boot-security-jwt-0.0.1-SNAPSHOT.jar"]