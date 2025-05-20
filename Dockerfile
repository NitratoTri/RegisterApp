# Etapa de build
FROM maven:3.8.7-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa de ejecución
FROM openjdk:17.0.1-jdk-slim
WORKDIR /app
COPY --from=build /app/target/RegisterApp-0.0.1-SNAPSHOT.jar app.jar

# Expón el puerto (esto es solo informativo para Docker/Railway)
EXPOSE 8080

# Usa la variable de entorno PORT si está definida (opcional, para debugging)
ENV PORT=8080

ENTRYPOINT ["java", "-jar", "app.jar"]
