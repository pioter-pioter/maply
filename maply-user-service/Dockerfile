FROM openjdk:18

WORKDIR /maply/maply-user-service

COPY .mvn .mvn
COPY src src
COPY pom.xml .
COPY mvnw .
COPY wait-for-it.sh .

RUN chmod +x wait-for-it.sh
RUN chmod +x mvnw

RUN ./mvnw package -DskipTests

ENTRYPOINT ["./wait-for-it.sh", "db:3306", "--", "java", "-jar", "target/maply-user-service-0.0.1-SNAPSHOT.jar"]