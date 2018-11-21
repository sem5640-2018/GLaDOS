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
RUN echo "Exporting project..." && mvn clean && mvn war:war

# Creates the resulting image
FROM payara/micro:prerelease

COPY --from=builder /app/target/glados*.war /opt/payara/deployments/glados.war