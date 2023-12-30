FROM openjdk:17
VOLUME /tmp
EXPOSE 8081
ARG JAR_FILE=target/gw-springboot-keycloak-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} gw-springboot-keycloak.jar
ENTRYPOINT ["java","-jar","/gw-springboot-keycloak.jar"]
