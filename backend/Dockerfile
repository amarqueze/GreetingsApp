FROM gradle:7.3.3-jdk11-alpine AS builder

WORKDIR /home

COPY . .
RUN gradle build

FROM adoptopenjdk:11-jre-hotspot

COPY --from=builder /home/app/build/libs .

ENV JAVA_OPTS="-XX:+UseContainerSupport -Djava.security.egd=file:/dev/./urandom"
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar greetings_app-RELEASE.jar"]