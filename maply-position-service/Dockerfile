FROM openjdk:18

WORKDIR /maply/maply-position-service

COPY .mvn .mvn
COPY src src
COPY pom.xml .
COPY mvnw .
COPY wait-for-it.sh .

RUN chmod +x wait-for-it.sh
RUN chmod +x mvnw

RUN ./mvnw package

ENTRYPOINT ["./wait-for-it.sh", "db:6379", "--", "java", "-jar", "target/maply-0.0.1-SNAPSHOT.jar"]