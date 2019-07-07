FROM maven:3.6.1-jdk-8 AS build

# copy pom.xml and get the dependencies
COPY trace-api/ /usr/trace-api/
RUN mvn dependency:go-offline -f /usr/trace-api/pom.xml

# Run install
RUN mvn install -f /usr/trace-api/pom.xml

# copy pom.xml and get the dependencies
COPY service-spring/pom.xml /usr/service-spring/pom.xml
RUN mvn dependency:go-offline -f /usr/service-spring/pom.xml

# copy the src code and package application
COPY service-spring/src /usr/service-spring/src
RUN mvn -f /usr/service-spring/pom.xml clean package

# from build machine copy jar and execute
FROM openjdk:8-jdk-alpine3.9

COPY --from=build /usr/service-spring/target/service-spring.jar /usr/app/service-spring.jar
ENTRYPOINT ["java","-jar","/usr/app/service-spring.jar"]