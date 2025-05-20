# Etapa de build
FROM maven:3.8.7-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa de ejecución
FROM openjdk:17.0.1-jdk-slim
WORKDIR /app
COPY --from=build /app/target/RegisterApp-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENV PORT=8080

# Limita la memoria máxima de la JVM a 2 GB
ENTRYPOINT ["java", "-Xmx2g", "-jar", "app.jar"]
