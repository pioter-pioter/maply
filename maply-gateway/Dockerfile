FROM openjdk:11

WORKDIR /maply/maply-gateway

COPY .mvn .mvn
COPY src src
COPY pom.xml .
COPY mvnw .

RUN ./mvnw package

ENTRYPOINT ["java", "-jar", "target/maply-gateway-0.0.1-SNAPSHOT.jar"]