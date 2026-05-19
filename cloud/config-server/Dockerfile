FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

COPY . .

#RUN #mvn clean package spring-boot:repackage -DskipTests
RUN mvn clean install -DskipTests -T 1C

RUN mvn -pl cloud/config-server -am clean package spring-boot:repackage -DskipTests



FROM eclipse-temurin:17-jre-jammy

#FROM eclipse-temurin:21-jdk
#FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

COPY --from=build /app/cloud/config-server/target/*.jar app.jar

EXPOSE 4002

ENTRYPOINT ["java", "-jar", "app.jar"]
