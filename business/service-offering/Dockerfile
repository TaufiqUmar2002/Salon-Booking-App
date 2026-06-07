FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app
COPY . .

RUN mvn clean package spring-boot:repackage -DskipTests

#FROM eclipse-temurin:21-jdk
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

COPY --from=build /app/business/service-offering/target/service-offering-0.0.1-SNAPSHOT.jar /app/app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
