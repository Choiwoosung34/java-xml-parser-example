FROM openjdk:8
VOLUME /tmp
ARG JAR_FILE=target/com.sample.xmlparser-0.0.1-SNAPSHOT.jar

ENV SPRING_PROFILES_ACTIVE=local
# local, dev, stage, product

COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]
EXPOSE 9000
