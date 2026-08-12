FROM maven:3.9.11-eclipse-temurin-21 AS build

WORKDIR /workspace

COPY pom.xml .

RUN mvn install -N -DskipTests

COPY security-common/pom.xml security-common/pom.xml
COPY security-common/src security-common/src

RUN mvn -q -f security-common/pom.xml clean install -DskipTests

COPY loan-service/pom.xml loan-service/pom.xml
COPY loan-service/src loan-service/src

RUN mvn -q -f loan-service/pom.xml clean package -DskipTests


FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /workspace/loan-service/target/loan-service-1.0.0.jar app.jar

EXPOSE 8083

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-jar", "/app/app.jar"]