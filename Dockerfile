# Builder image which tests locally
FROM alpine:latest as builder
ARG RUN_TESTS

RUN apk update
RUN apk add maven openjdk8

# Copy build files
RUN mkdir /app
WORKDIR /app
RUN mkdir src
COPY src src
COPY pom.xml .

# Maven Stages
RUN ${RUN_TESTS} && echo "Running tests...." && mvn test -B

# Prepare war for packaging step
RUN echo "Exporting project..." && mvn clean && mvn compile package

# Creates the resulting image
FROM payara/micro:prerelease

ENV DB_HOSTNAME mariadb
ENV DB_USERNAME root
ENV DB_PASSWORD test

# JDBC
RUN wget -nv -O /opt/payara/mariadb-jdbc.jar https://downloads.mariadb.com/Connectors/java/connector-java-2.3.0/mariadb-java-client-2.3.0.jar 

COPY --from=builder /app/target/glados*.war /opt/payara/glados.war

ENTRYPOINT ["java", "-jar", "payara-micro.jar", "--addJars" ,"/opt/payara/mariadb-jdbc.jar" , "--deploy", "/opt/payara/glados.war" ]