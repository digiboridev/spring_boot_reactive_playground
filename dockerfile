# syntax=docker/dockerfile:1

# Full build
FROM eclipse-temurin:21
WORKDIR /app
COPY . .
RUN ./gradlew bootJar
EXPOSE 8080
CMD ["java", "-jar","build/app.jar"]

# Lightweight debug build from local pre-built jar
# don't forget to run ./gradlew build before building the image
# FROM eclipse-temurin:21
# WORKDIR /app
# COPY build .
# EXPOSE 8080
# CMD ["java", "-jar","app.jar"]