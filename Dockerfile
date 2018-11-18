# Builder image which tests locally
FROM alpine:latest as builder
ARG RUN_TESTS

RUN apk update
RUN apk add maven openjdk8 syslog-ng

COPY bin/syslog-ng.conf /etc/syslog/syslog-ng.conf

RUN syslog-ng -f /etc/syslog/syslog-ng.conf

# Copy build files
RUN mkdir /app
WORKDIR /app
RUN mkdir src
COPY src src
COPY pom.xml .

# Maven Stages
RUN export SYSLOG_HOST=127.0.0.1
RUN mvn dependency:go-offline
RUN ${RUN_TESTS} && echo "Running tests...." && mvn test -B

# Prepare exploded war for packaging step
RUN echo "Exporting project..." && mvn war:exploded

# Creates the resulting image
FROM payara/micro
ADD https://downloads.mariadb.com/Connectors/java/connector-java-2.3.0/mariadb-java-client-2.3.0.jar /opt/payara/glassfish/lib/mariadb-java-client-2.3.0.jar
COPY --from=builder /app/target/glados-exploded /opt/payara/deployments/glados
CMD [ java -jar payara-micro.jar --deploy /opt/payara/deployments/glados ]