FROM maven:3.9.11-eclipse-temurin-21 AS build

WORKDIR /workspace

COPY pom.xml .

RUN mvn install -N -DskipTests

COPY security-common/pom.xml security-common/pom.xml
COPY security-common/src security-common/src

RUN mvn -q -f security-common/pom.xml clean install -DskipTests

COPY reservation-service/pom.xml reservation-service/pom.xml
COPY reservation-service/src reservation-service/src

RUN mvn -q -f reservation-service/pom.xml clean package -DskipTests


FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /workspace/reservation-service/target/reservation-service-1.0.0.jar app.jar

EXPOSE 8084

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-jar", "/app/app.jar"]