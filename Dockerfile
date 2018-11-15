# Builder image which tests locally
FROM alpine:latest as builder
ARG RUN_TESTS

COPY bin/syslog-ng.conf /etc/syslog/syslog-ng.conf

RUN apk update
RUN apk add maven openjdk8 syslog-ng

# Copy build files
RUN mkdir /app
WORKDIR /app
COPY src .
COPY pom.xml .

# Maven Stages
RUN mvn dependency:resolve

RUN ${RUN_TESTS} && echo "Running tests...." && mvn test

# Prepare exploded war for packaging step
RUN echo "Exporting project..." && mvn war:exploded

# Creates the resulting image
FROM payara/micro
COPY --from=builder /app/target/glados-exploded /opt/payara/deployments/glados
CMD [ java -jar payara-micro.jar --deploy /opt/payara/deployments/glados ]