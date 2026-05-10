FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app
COPY . .


# 🔥 STEP 1: build EVERYTHING in correct order
#RUN mvn clean install -DskipTests
#
## 🔥 STEP 2: build only service
#RUN mvn -pl business/salon-service -am package -DskipTests

RUN mvn clean install -DskipTests -T 1C

RUN mvn -pl business/user-service -am clean package spring-boot:repackage -DskipTests



FROM eclipse-temurin:17-jre-jammy
#RUN mvn clean package spring-boot:repackage -DskipTests
RUN #mvn -pl business/user-service -am clean package -DskipTests

#FROM eclipse-temurin:21-jdk
#FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

COPY --from=build /app/business/user-service/target/user-service-0.0.1-SNAPSHOT.jar /app/app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
