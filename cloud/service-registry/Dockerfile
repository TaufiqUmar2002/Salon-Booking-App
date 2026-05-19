FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app
COPY . .

RUN mvn clean install -DskipTests -T 1C

RUN mvn -pl cloud/service-registry -am clean package spring-boot:repackage -DskipTests


FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=build /app/cloud/service-registry/target/*.jar app.jar

EXPOSE 4001

ENTRYPOINT ["java", "-jar", "app.jar"]
