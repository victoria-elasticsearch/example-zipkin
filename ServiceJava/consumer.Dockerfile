FROM maven:3.6.1-jdk-8 AS build

# copy pom.xml and get the dependencies
COPY trace-api/ /usr/trace-api/
RUN mvn dependency:go-offline -f /usr/trace-api/pom.xml

# Run install
RUN mvn install -f /usr/trace-api/pom.xml

# copy pom.xml and get the dependencies
COPY spring-consumer/pom.xml /usr/spring-consumer/pom.xml
RUN mvn dependency:go-offline -f /usr/spring-consumer/pom.xml

# copy the src code and package application
COPY spring-consumer/src /usr/spring-consumer/src
RUN mvn -f /usr/spring-consumer/pom.xml clean package

# from build machine copy jar and execute
FROM openjdk:8-jdk-alpine3.9

COPY --from=build /usr/spring-consumer/target/spring-consumer.jar /usr/app/spring-consumer.jar
ENTRYPOINT ["java","-jar","/usr/app/spring-consumer.jar"]